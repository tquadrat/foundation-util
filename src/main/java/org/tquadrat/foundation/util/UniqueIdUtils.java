/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.util;

import static java.lang.Long.toBinaryString;
import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;
import static java.util.Locale.ROOT;
import static java.util.UUID.fromString;
import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotBlankArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.lang.Objects.requireValidIntegerArgument;
import static org.tquadrat.foundation.util.Base32.getDecoder;
import static org.tquadrat.foundation.util.SecurityUtils.calculateMD5Hash;
import static org.tquadrat.foundation.util.SecurityUtils.calculateSHA1Hash;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.util.StringUtils.repeat;
import static org.tquadrat.foundation.util.StringUtils.splitString;
import static org.tquadrat.foundation.util.SystemUtils.createPseudoNodeId;
import static org.tquadrat.foundation.util.SystemUtils.currentTimeNanos;
import static org.tquadrat.foundation.util.SystemUtils.getNodeId;
import static org.tquadrat.foundation.util.SystemUtils.getRandom;
import static org.tquadrat.foundation.util.SystemUtils.repose;
import static org.tquadrat.foundation.util.TSID.MAX_IDS_PER_MILLISECOND;
import static org.tquadrat.foundation.util.TSID.MAX_NODES;
import static org.tquadrat.foundation.util.internal.TSIDImpl.SHIFT;
import static org.tquadrat.foundation.util.internal.TSIDImpl.TSID_Node;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.UnsupportedEnumError;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.lang.AutoLock;
import org.tquadrat.foundation.util.Base32.Decoder;
import org.tquadrat.foundation.util.internal.TSIDImpl;

/**
 *  <p>{@summary This static class provides some utility methods that are helpful when
 *  working with unique ids.}</p>
 *  <p>All methods in this class are final, no instance of this class is
 *  allowed.</p>
 *  <p>First it extends the capabilities of the class
 *  {@link UUID}
 *  that is a part of the Java Runtime library; it implements Universal Unique
 *  ids as defined through RFC&nbsp;4122. It extends the
 *  capabilities of the Java Runtime class
 *  {@link UUID}.</p>
 *
 *  <h2>RFC&nbsp;4122 UUID</h2>
 *  <p>The methods
 *  {@link #nameUUIDFromBytes(byte[],HashType)},
 *  {@link #nameUUIDFromString(CharSequence,HashType)},
 *  {@link #nameUUIDFromString(UUID, CharSequence,HashType)},
 *  {@link #randomUUID()},
 *  {@link #sequenceUUID(long,long)},
 *  {@link #timebasedUUID()},
 *  {@link #timebasedUUID(long)},
 *  {@link #timebasedUUIDFromNodeName(CharSequence)},
 *  and
 *  {@link #uuidFromString(CharSequence)}
 *  do all create a
 *  {@link UUID}
 *  instance, but {@code randomUUID()} will delegate to the method with the
 *  same name of the class {@code UUID} itself, while
 *  {@code uuidFromString(CharSequence)} delegates to
 *  {@link UUID#fromString(String)}.
 *  {@code nameUUIDFromBytes(byte[],HashType)} delegates to
 *  {@link UUID#nameUUIDFromBytes(byte[])}
 *  for {@code hashType} equal to
 *  {@link HashType#HASH_MD5}.</p>
 *  <p>Currently, this class supports only the generation of UUIDs with the
 *  types&nbsp;1 (not supported by
 *  {@link java.util.UUID}),
 *  3, 4, and 5, although the method
 *  {@link #uuidFromString(CharSequence)}
 *  is also capable of converting UUID Strings representing the type&nbsp;2
 *  into valid UUID instances.</p>
 *  <p>The type 0 as generated by
 *  {@link #sequenceUUID(long,long)}
 *  is not defined by RFC&nbsp;4122.</p>
 *
 *  <h3>The sample Implementation for a UUID Generator</h3>
 *  <p>The source code for this sample implementation in C was taken from
 *  <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC&nbsp;4122</a>.</p>
 *  <ul>
 *  <li><a href="doc-files/uuid.h"><code>uuid.h</code></a></li>
 *  <li><a href="doc-files/uuid.c"><code>uuid.c</code></a></li>
 *  <li><a href="doc-files/sysdep.h"><code>sysdep.h</code></a></li>
 *  <li><a href="doc-files/sysdep.c"><code>sysdep.c</code></a></li>
 *  <li><a href="doc-files/utest.c"><code>utest.c</code></a></li>
 *  <li><a href="doc-files/copyrt.h"><code>copyrt.h</code></a></li>
 *  </ul>
 *  <p>The appendix C of RFC&nbsp;4122 also lists the name space IDs for some
 *  potentially interesting name spaces, as initialized C structures and in the
 *  string representation defined by the RFC.</p>
 *  <pre><code>   &#47;* Name string is a fully-qualified domain name *&#47;
 *   uuid_t NameSpace_DNS = { &#47;* 6ba7b810-9dad-11d1-80b4-00c04fd430c8 *&#47;
 *       0x6ba7b810,
 *       0x9dad,
 *       0x11d1,
 *       0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8
 *   };
 *
 *   &#47;* Name string is a URL *&#47;
 *   uuid_t NameSpace_URL = { &#47;* 6ba7b811-9dad-11d1-80b4-00c04fd430c8 *&#47;
 *       0x6ba7b811,
 *       0x9dad,
 *       0x11d1,
 *       0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8
 *   };
 *
 *   &#47;* Name string is an ISO OID *&#47;
 *   uuid_t NameSpace_OID = { &#47;* 6ba7b812-9dad-11d1-80b4-00c04fd430c8 *&#47;
 *       0x6ba7b812,
 *       0x9dad,
 *       0x11d1,
 *       0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8
 *   };
 *
 *   &#47;* Name string is an X.500 DN (in DER or a text output format) *&#47;
 *   uuid_t NameSpace_X500 = { &#47;* 6ba7b814-9dad-11d1-80b4-00c04fd430c8 *&#47;
 *       0x6ba7b814,
 *       0x9dad,
 *       0x11d1,
 *       0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8
 *   };</code></pre>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UniqueIdUtils.java 1078 2023-10-19 14:39:47Z tquadrat $
 *  @since 0.0.5
 *
 *  @see UUID#nameUUIDFromBytes(byte[])
 *  @see UUID#randomUUID()
 *  @see UUID#fromString(String)
 *  @see <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@ClassVersion( sourceVersion = "$Id: UniqueIdUtils.java 1078 2023-10-19 14:39:47Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
@UtilityClass
public final class UniqueIdUtils
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  Two different hash types are used for name-based UUIDs.
     *
     *   @UMLGraph.link
     */
    public static enum HashType
    {
        /**
         *  UUIDs of type 3 are using MD5 hashes.
         */
        HASH_MD5,

        /**
         *  UUIDs of type 5 are using SHA-1 hashes.
         */
        HASH_SHA
    }
    //  enum HashType

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The bit mask used for the conversion from and to a number.
     */
    private static final BigInteger BIT_MASK = BigInteger.valueOf( 0xFFFFFFFFFFFFFFFFL );

    /**
     *  Divisor for the calculation of the timestamp.
     */
    private static final BigInteger ONE_HUNDRED = BigInteger.valueOf( 100 );

    /**
     *  Factor for the conversion of milliseconds to nanoseconds.
     */
    private static final BigInteger ONE_MILLION = BigInteger.valueOf( 1_000_000 );

    /**
     *  <p>{@summary The name for the internal system property for the flag
     *  controlling that only pseudo node ids should be used to generate
     *  {@link UUID UUID}
     *  instances of type 1: {@value}.}</p>
     *  <p>A value of {@code true} means that only pseudo ids will be used
     *  throughout the current run of the program, while {@code false}
     *  (the default) means that a MAC address is used for the calculation of a
     *  node id, if available.</p>
     *  <p>This system property is not necessarily configured.</p>
     *
     *  @see SystemUtils#PROPERTY_NODE_ID
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String PROPERTY_USE_PSEUDO_NODE_ID = "org.tquadrat.foundation.util.UniqueIdUtils.UsePseudoNodeId";

    /**
     *  The character count for a {@link TSID}: {@value}.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    public static final int TSID_Size = TSID.TSID_Size;

    /**
     *  The character count for a {@link UUID}: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int UUID_Size = 36;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The clock sequence.
     */
    private static volatile long m_ClockSeq = Long.MIN_VALUE;

    /**
     *  The last time when a clock sequence was requested.
     */
    private static volatile long m_LastClockSeqRequest = 0L;

    /**
     *  The millisecond when the last TSID was created.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    private static volatile long m_LastTSIDCreationTime = -1;

    /**
     *  The counter TSIDs that were created in the current millisecond.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    private static volatile int m_TSIDCounter = 0;

    /**
     *  The counter for version 7 UUIDs.
     */
    private static final AtomicInteger m_UUID7Counter = new AtomicInteger( getRandom().nextInt() );

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  <p>{@summary The digits that are used for an XML safe UUID.} The first
     *  array holds the original digits, the second array those for the XML
     *  id.</p>
     */
    @API( status = INTERNAL, since = "0.3.0" )
    private static final char [][] m_UUIDXMLDigits;

    static
    {
        @SuppressWarnings( "SpellCheckingInspection" )
        final var fromXML = "-0123456789ABCDEFGHIJKL".toCharArray();
        @SuppressWarnings( "SpellCheckingInspection" )
        final var toXML = "XABCDEFGHJKLMNPRSTUVWYZ".toCharArray();
        m_UUIDXMLDigits = new char[2][fromXML.length];
        m_UUIDXMLDigits [0] = fromXML;
        m_UUIDXMLDigits [1] = toXML;
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance of this class is allowed!
     */
    private UniqueIdUtils() { throw new PrivateConstructorForStaticClassCalledError( UniqueIdUtils.class ); }

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The Base&nbsp;32 decoder for the TSIDs.
     *
     *  @see #tsidFromString(CharSequence)
     */
    private static final Decoder m_Base32Decoder;

    /**
     *  The guard for the clock sequence.
     */
    private static final AutoLock m_ClockSeqGuard;

    /**
     *  The dummy node id that is used to generate UUIDs, if required. This is
     *  always a random value.
     */
    private static final long m_DummyNodeId;

    /**
     *  The UUIDs for the predefined name spaces, according to RFC 4122.
     */
    @SuppressWarnings( "StaticCollection" )
    private static final Map<String,UUID> m_Namespaces;

    /**
     *  The node id that is used to generate UUIDs. This is either the MAC
     *  address of one of the NICs in the current system, or a random value.
     */
    private static final long m_NodeId;

    /**
     *  <p>{@summary The guard for the field that are used for the creation of a
     *  {@link TSID}.}</p>
     *  <p>These are:</p>
     *  <ul>
     *      <li>{@link #m_LastTSIDCreationTime}</li>
     *      <li>{@link #m_TSIDCounter}</li>
     *  </ul>
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @SuppressWarnings( "DeprecatedIsStillUsed" )
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    private static final AutoLock m_TSIDGuard;

    /**
     *  This flag controls if
     *  {@link #m_NodeId}
     *  is forced to be a random value.<br>
     *  <br>It will be controlled by the system property
     *  &quot;{@value #PROPERTY_USE_PSEUDO_NODE_ID}&quot;.<br>
     *  <br>Using a pseudo node id would generate anonymous UUIDs.
     */
    private static final boolean m_UsePseudoNodeId;

    /**
     *  The max UUID.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID UUID_MAX;

    /**
     *  The nil UUID.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID UUID_NIL;

    static
    {
        //---* Create the Base 32 decoder *------------------------------------
        m_Base32Decoder = getDecoder();

        //---* Create the locks *----------------------------------------------
        m_ClockSeqGuard = AutoLock.of();
        m_TSIDGuard = AutoLock.of();

        //---* Create the dummy node id *--------------------------------------
        m_DummyNodeId = createPseudoNodeId();

        //---* Get the flag that controls the generation of the node id *------
        m_UsePseudoNodeId = Boolean.getBoolean( PROPERTY_USE_PSEUDO_NODE_ID );

        //---* Retrieve the node id *------------------------------------------
        m_NodeId = m_UsePseudoNodeId ? createPseudoNodeId() : getNodeId();

        //---* Create the namespaces *-----------------------------------------
        final Map<String,UUID> namespaces = new TreeMap<>();

        namespaces.put( "DNS", fromString( "6ba7b810-9dad-11d1-80b4-00c04fd430c8" ) );
        namespaces.put( "URL", fromString( "6ba7b811-9dad-11d1-80b4-00c04fd430c8" ) );
        namespaces.put( "ISO_OID", fromString( "6ba7b812-9dad-11d1-80b4-00c04fd430c8" ) );
        namespaces.put( "X500", fromString( "6ba7b814-9dad-11d1-80b4-00c04fd430c8" ) );

        var internalNamespace = "tquadrat";
        namespaces.put( internalNamespace, UUID.nameUUIDFromBytes( internalNamespace.getBytes( UTF8 ) ) );
        internalNamespace = "Foundation";
        namespaces.put( internalNamespace, UUID.nameUUIDFromBytes( internalNamespace.getBytes( UTF8 ) ) );

        m_Namespaces = Map.copyOf( namespaces );

        //---* The UUIDs *-----------------------------------------------------
        UUID_NIL = new UUID( 0, 0 );
        UUID_MAX = new UUID( 0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL );
    }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts an XML safe id that was created through
     *  {@link #toXMLId(UUID)}
     *  back to a UUID.
     *
     *  @param  input   The XML safe id.
     *  @return The UUID.
     *  @throws IllegalArgumentException    The given XML safe id cannot be
     *      converted to a UUID.
     */
    @API( status = STABLE, since = "0.3.0" )
    public static final UUID fromXMLId( final CharSequence input )
    {
        final var radix = m_UUIDXMLDigits [0].length - 1;
        final var numbers = new long [2];

        final var parts = splitString( requireNotBlankArgument( input, "input" ).toString().toUpperCase( ROOT ), "-" );
        final var message = "Cannot convert '%s' to a UUID!".formatted( input );
        requireValidIntegerArgument( parts.length, "input", length -> length == 2, $ -> message );
        for( var i = 0; i < parts.length; ++i )
        {
            final var buffer = new StringBuilder();
            for( final var c : parts [i].toUpperCase( ROOT ).toCharArray() )
            {
                IntStream.range( 0, m_UUIDXMLDigits[1].length )
                    .filter( index -> m_UUIDXMLDigits[1][index] == c )
                    .findFirst()
                    .ifPresentOrElse( index -> buffer.append( m_UUIDXMLDigits[0][index] ), () -> {throw new IllegalArgumentException( message );} );
            }
            numbers [i] = Long.parseLong( buffer.toString().toLowerCase( ROOT ), radix );
        }

        final var retValue = new UUID( numbers [0], numbers [1] );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromXMLId()

    /**
     *  <p>{@summary Returns the clock sequence.} It will be initialised with a
     *  random number on each time the program starts, and it remains unchanged
     *  until the system detects a clock shift; in that case, it will be
     *  increased by one.</p>
     *  <p>An overflow for the clock sequence is possible, but does not harm.</p>
     *
     *  @param  currentTime The current time.
     *  @return The clock sequence.
     */
    private static final long getClockSequence( final long currentTime )
    {
        final long clockSeq;
        try( final var ignored = m_ClockSeqGuard.lock() )
        {
            if( m_ClockSeq == Long.MIN_VALUE )
            {
                //---* Initialise the clock sequence *-------------------------
                m_ClockSeq = abs( getRandom().nextLong() );
            }
            else if( currentTime <= m_LastClockSeqRequest )
            {
                //noinspection NonAtomicOperationOnVolatileField
                ++m_ClockSeq;
            }
            m_LastClockSeqRequest = currentTime;
            clockSeq = m_ClockSeq;
        }

        final var retValue = (clockSeq & 0x0000000000003FFFL) << 48;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // getClockSequence()

    /**
     *  Returns the UUID for the namespace with the given name.
     *
     *  @param  key The name of the namespace.
     *  @return The UUID for the namespace, or {@code null} if that namespace
     *      does not exist.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID getNamespaceUUID( final String key ) { return m_Namespaces.get( requireNotEmptyArgument( key, "key" ) ); }

    /**
     *  Returns the names of the known UUID namespaces.
     *
     *  @return The names of the namespaces.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String [] listNamespaces()
    {
        final var retValue = m_Namespaces.keySet().toArray( EMPTY_String_ARRAY );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  listNamespaces()

    /**
     *  Static factory to retrieve a type&nbsp;3 (name based, MD5 hashed) or a
     *  type&nbsp;5 (name based, SHA hashed) UUID based on the specified byte
     *  array.<br>
     *  <br>This method will always return the same output if the input is the
     *  same.<br>
     *  <br>The provided name should be prepended with the UUID for a
     *  designated name space, although this is neither enforced nor checked by
     *  this method.
     *
     *  @param  name    A byte array to be used to construct a UUID.
     *  @param  hashType    The hash type to use.
     *  @return The UUID generated from the specified array.
     *
     *  @see UUID#nameUUIDFromBytes(byte[])
     */
    @SuppressWarnings( {"MagicNumber", "ImplicitNumericConversion"} )
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID nameUUIDFromBytes( final byte [] name, final HashType hashType )
    {
        requireNonNullArgument( name, "name" );

        final var retValue = switch( requireNonNullArgument( hashType, "hashType" ) )
        {
            case HASH_MD5 -> UUID.nameUUIDFromBytes( name );
            case HASH_SHA -> {
                final var shaBytes = calculateSHA1Hash( name );
                shaBytes[6] &= 0x0f; // Clear version
                shaBytes[6] |= 0x50; // Set to version 5
                shaBytes[8] &= 0x3f; // Clear variant
                //noinspection lossy-conversions
                shaBytes[8] |= 0x80; // Set to IETF variant

                var mostSigBits = 0L;
                var leastSigBits = 0L;
                for( var i = 0; i < 8; ++i )
                {
                    mostSigBits |= ((long) (shaBytes[7 - i] & 0xff)) << (i << 3);
                    leastSigBits |= ((long) (shaBytes[15 - i] & 0xff)) << (i << 3);
                }
                yield new UUID( mostSigBits, leastSigBits );
            }
            default -> throw new UnsupportedEnumError( hashType );
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  nameUUIDFromBytes()

    /**
     *  Creates a name-based (version type&nbsp;3 or type&nbsp;5, depending on
     *  the provided hash type) UUID from the given String.<br>
     *  <br>This method will always return the same output if the input is the
     *  same.<br>
     *  <br>The provided name should be prepended with the UUID for a
     *  designated name space, although this is neither enforced nor checked by
     *  this method.
     *
     *  @param  name    The name base for the UUID.
     *  @param  hashType    The hash type to use.
     *  @return The UUID.
     *
     *  @see UUID#nameUUIDFromBytes(byte[])
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID nameUUIDFromString( final CharSequence name, final HashType hashType )
    {
        //---* Get the byte array *--------------------------------------------
        final var bytes = requireNonNullArgument( name, "name" ).toString().getBytes( UTF8 );

        //---* Create the UUID *-----------------------------------------------
        final var retValue = nameUUIDFromBytes( bytes, hashType );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // nameUUIDFromString()

    /**
     *  Creates a name-based (version type&nbsp;3 or type&nbsp;5, depending on
     *  the provided hash type) UUID from the given String, using the
     *  provided namespace UUID as the prefix.<br>
     *  <br>This method will always return the same output if the input is the
     *  same.
     *
     *  @param  namespace   The UUID for the namespace.
     *  @param  hashType    The hash type to use.
     *  @param  name    The name base for the UUID.
     *  @return The UUID.
     *
     *  @see UUID#nameUUIDFromBytes(byte[])
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID nameUUIDFromString( final UUID namespace, final CharSequence name, final HashType hashType )
    {
        final var namespaceName = requireNonNullArgument( namespace, "namespace" ).toString() + requireNonNullArgument( name, "name" );

        //---* Create the UUID *-----------------------------------------------
        final var retValue = nameUUIDFromBytes( namespaceName.getBytes( UTF8 ), hashType );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // nameUUIDFromString()

    /**
     *  <p>{@summary Creates a new
     *  {@link TSID}
     *  instance.}</p>
     *
     *  @return The {@code TSID} instance.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    public static final TSID newTSID()
    {
        final var retValue = newTSIDInternal( TSID_Node );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  newTSID()

    /**
     *  <p>{@summary Creates a new
     *  {@link TSID}
     *  instance.}</p>
     *
     *  @param  nodeId  The node id; a number between 0 (included) and 1024
     *      (excluded).
     *  @return The {@code TSID} instance.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    public static final TSID newTSID( final int nodeId )
    {
        if( 0 > nodeId ) throw new ValidationException( "nodeId '%d' is less than zero".formatted( nodeId ) );
        if( nodeId >= MAX_NODES )
        {
            //noinspection ConstantExpression
            throw new ValidationException( "nodeId '%d' is greater than %d".formatted( nodeId, MAX_NODES - 1 ) );
        }

        final var retValue = newTSIDInternal( (long) nodeId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  newTSID()

    /**
     *  <p>{@summary Creates a new
     *  {@link TSID}
     *  instance without checking the {@code nodeId} argument.} Called only by
     *  {@link #newTSID()}
     *  and
     *  {@link #newTSID(int)}.</p>
     *
     *  @param  nodeId  The node id; a number between 0 (included) and 1024
     *      (excluded).
     *  @return The {@code TSID} instance.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    private static final TSID newTSIDInternal( final long nodeId )
    {
        final long counter;
        long currentTime;

        try( final var ignored = m_TSIDGuard.lock() )
        {
            currentTime = currentTimeNanos().divide( ONE_MILLION ).longValue() - SHIFT;

            //---* Wait for a valid time and counter *-------------------------
            while( (m_TSIDCounter >= MAX_IDS_PER_MILLISECOND) && (currentTime <= m_LastTSIDCreationTime) )
            {
                currentTime = currentTimeNanos().divide( ONE_MILLION ).longValue() - SHIFT;
            }

            //---* Update the counter *----------------------------------------
            if( currentTime > m_LastTSIDCreationTime )
            {
                m_TSIDCounter = 0;
            }
            else
            {
                //noinspection NonAtomicOperationOnVolatileField
                ++m_TSIDCounter;
            }

            //---* Keep the time *---------------------------------------------
            m_LastTSIDCreationTime = currentTime;

            //---* Keep the counter *------------------------------------------
            counter = (long) m_TSIDCounter;
        }

        final var id = (currentTime << 22) | (counter << 10) | nodeId;
        final var retValue = new TSIDImpl( id );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  newTSIDInternal()

    /**
     *  Static factory to retrieve a type 4 (pseudo randomly generated) UUID.
     *  The UUID is generated using a cryptographically strong pseudo random
     *  number generator.<br>
     *  <br>This is a wrapper for the method with the same name from
     *  {@link UUID}.
     *
     *  @return A randomly generated UUID.
     *
     *  @see UUID#randomUUID()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID randomUUID() { return UUID.randomUUID(); }

    /**
     *  <p>{@summary Creates a sequence UUID from the given values; this UUID
     *  will have the type 0 (that is not officially defined).}</p>
     *  <p>UUIDs of this type are used to define globally identical keys,
     *  meaning that this method will always return the same output if the
     *  input is the same.</p>
     *
     *  @param  mostSignificant The most significant bits for the new UUID.
     *  @param  leastSignificant    The least significant bits for the new
     *      UUID.
     * @return The new UUID of type 0.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID sequenceUUID( final long mostSignificant, final long leastSignificant )
    {
        //---* Calculate the most significant bits *---------------------------
        @SuppressWarnings( "OverlyComplexBooleanExpression" )
        final var timeLow = ( (mostSignificant << 44) & 0xFFFFF00000000000L) | ( (leastSignificant >> 20) & 0x00000FFF00000000L);
        final var timeMid = (mostSignificant >> 8) & 0x00000000FFFF0000L;
        final var timeHi = (mostSignificant >> 24) & 0x0000000000000FFFL;
        final var mostSigBits = timeLow | timeMid | timeHi;

        //---* Calculate the least significant bits *--------------------------
        final var variant = (0x2L << 62) & 0x8000000000000000L;
        @SuppressWarnings( "OverlyComplexBooleanExpression" )
        final var leastSigBits = variant | ( (mostSignificant & 0x03FF000000000000L) << 4) | (leastSignificant & 0x000FFFFFFFFFFFFFL);

        //---* Create the UUID *-----------------------------------------------
        final var retValue = new UUID( mostSigBits, leastSigBits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // sequenceUUID()

    /**
     *  Creates a time-based (version type 1) UUID, using the given node id.
     *
     *  @param  nodeId  The node id; only the lower 48 bit from this value are
     *      used for the UUID.
     *  @return The UUID.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID timebasedUUID( final long nodeId )
    {
        //---* Calculate the most significant bits *---------------------------
        final var currentTime = currentTimeNanos().divide( ONE_HUNDRED ).longValue();
        final var timeLow = (currentTime << 32) & 0xFFFFFFFF00000000L;
        final var timeMid = (currentTime >> 16) & 0x00000000FFFF0000L;
        final var version = 4096L; //(1 << 12) & 0x000000000000F000L;
        final var timeHi = (currentTime >> 48) & 0x0000000000000FFFL;
        final var mostSigBits = timeLow | timeMid | version | timeHi;

        //---* Calculate the least significant bits *--------------------------
        final var variant = (0x2L << 62) & 0x8000000000000000L;
        @SuppressWarnings( "OverlyComplexBooleanExpression" )
        final var leastSigBits = variant | getClockSequence( currentTime ) | (nodeId & 0x0000FFFFFFFFFFFFL);

        //---* Create the UUID *-----------------------------------------------
        final var retValue = new UUID( mostSigBits, leastSigBits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // timebasedUUID()

    /**
     *  Creates a time-based (version type 1) UUID using the internal node id.
     *
     *  @return The UUID.
     *
     *  @see #m_NodeId
     *  @see #m_UsePseudoNodeId
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID timebasedUUID()
    {
        //---* Create the UUID *-----------------------------------------------
        final var retValue = timebasedUUID( m_NodeId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // timebasedUUID()

    /**
     *  Creates a time-based (version type 1) UUID from a dummy node id.
     *
     *  @return The UUID.
     */
    @API( status = STABLE, since = "0.0.7" )
    public static final UUID timebasedUUIDFromDummyNode()
    {
        //---* Create the UUID *-----------------------------------------------
        final var retValue = timebasedUUID( m_DummyNodeId );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // timebasedUUIDFromDummyNode()

    /**
     *  Creates a time-based (version type 1) UUID from the given node
     *  name.<br>
     *  <br>The provided node name will be hashed (using MD5), the bytes from
     *  the result will be converted into
     *  {@link BigInteger}. Then
     *  {@link #timebasedUUID(long)}
     *  is called with the result from
     *  {@link BigInteger#longValue()},
     *  called on the value mentioned before.
     *
     *  @param  nodeName    The node name.
     *  @return The UUID.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID timebasedUUIDFromNodeName( final CharSequence nodeName )
    {
        //---* Convert the node name to a numerical node id *------------------
        final var nodeId = new BigInteger( calculateMD5Hash( requireNonNullArgument( nodeName, "nodeName" ).toString().getBytes( UTF8 ) ) );

        //---* Create the UUID *-----------------------------------------------
        final var retValue = timebasedUUID( nodeId.longValue() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // timebasedUUIDFromNodeName()

    /**
     *  Converts a UUID to a String that can be used as an XML id.
     *
     *  @param  input   The UUID.
     *  @return The XML safe id.
     */
    @API( status = STABLE, since = "0.3.0" )
    public static final String toXMLId( final UUID input )
    {
        final var radix = m_UUIDXMLDigits [0].length - 1;
        final var numbers = new long [] {requireNonNullArgument( input, "input" ).getMostSignificantBits(), input.getLeastSignificantBits()};
        final var buffer = new StringBuilder();
        for( final var number : numbers )
        {
            if( isNotEmpty( buffer ) ) buffer.append( '-' );
            for( final var c : Long.toString( number, radix ).toUpperCase( ROOT ).toCharArray() )
            {
                IntStream.range( 0, m_UUIDXMLDigits[0].length )
                    .filter( index -> m_UUIDXMLDigits[0][index] == c )
                    .findFirst()
                    .ifPresent( index -> buffer.append( m_UUIDXMLDigits[1][index] ) );
            }
        }

        final var retValue = buffer.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toXMLId()

    /**
     *  <p>{@summary Creates a
     *  {@link TSID}
     *  from the given number.}</p>
     *
     *  @param  number  The TSID numerical value.
     *  @return The id.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    public static final TSID tsidFromNumber( final long number )
    {
        final var retValue = new TSIDImpl( number );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  tsidFromString()

    /**
     *  <p>{@summary Creates a
     *  {@link TSID}
     *  from the given String.} The TSID String is the
     *  {@href https://www.crockford.com/base32.html Crockfords's Base&nbsp;32}
     *  representation of the numerical id, prefixed by the letter 'X'.</p>
     *
     *  @param  input   The TSID String.
     *  @return The id.
     *  @throws ValidationException The provided String is not a valid TSID
     *      representation; it is either too short, does not start with 'X',
     *      or the suffix is not a valid Base&nbsp;32 String.
     *
     *  @deprecated Do not use any longer! This is not tested!
     */
    @SuppressWarnings( "DeprecatedIsStillUsed" )
    @Deprecated( since = "0.3.0", forRemoval = true )
    @API( status = DEPRECATED, since = "0.1.0" )
    public static final TSID tsidFromString( final CharSequence input )
    {
        if( requireNotBlankArgument( input, "input" ).length() != TSID_Size ) throw new ValidationException( "TSID String '%s' too short".formatted( input ) );
        if( !input.toString().toUpperCase( ROOT ).startsWith( "X" ) ) throw new ValidationException( "TSID String '%s' does not start with 'X'".formatted( input ) );

        final var number = m_Base32Decoder.decodeToNumber( input.toString().substring( 1 ) )
            .longValue();

        final var retValue = new TSIDImpl( number );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  tsidFromString()

    /**
     *  <p>{@summary Creates a
     *  {@link UUID}
     *  from the given number (more precise, the
     *  given
     *  {@link BigInteger}).}</p>
     *
     *  @param  value   The number.
     *  @return The UUID.
     */
    public static final UUID uuidFromNumber( final BigInteger value )
    {
        final var leastSignificantBits = requireNonNullArgument( value, "value" ).and( BIT_MASK ).longValue();
        final var mostSignificantBits = value.shiftRight( Long.SIZE ).and( BIT_MASK ).longValue();
        final var retValue = new UUID( mostSignificantBits, leastSignificantBits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  uuidFromNumber()

    /**
     *  <p>{@summary Creates a UUID from the string standard
     *  representation.}</p>
     *  <p>The UUID string representation is as described by this BNF:</p>
     *  <pre>
     *  UUID                   = &lt;time_low&gt; "-" &lt;time_mid&gt; "-"
     *                           &lt;time_high_and_version&gt; "-"
     *                           &lt;variant_and_sequence&gt; "-"
     *                           &lt;node&gt;
     *  time_low               = 4 &times; &lt;hexOctet&gt;
     *  time_mid               = 2 &times; &lt;hexOctet&gt;
     *  time_high_and_version  = 2 &times; &lt;hexOctet&gt;
     *  variant_and_sequence   = 2 &times; &lt;hexOctet&gt;
     *  node                   = 6 &times; &lt;hexOctet&gt;
     *  hexOctet               = &lt;hexDigit&gt;&lt;hexDigit&gt;
     *  hexDigit               =
     *        &quot;0&quot; | &quot;1&quot; | &quot;2&quot; | &quot;3&quot; | &quot;4&quot; | &quot;5&quot; | &quot;6&quot; | &quot;7&quot; | &quot;8&quot; | &quot;9&quot;
     *      | &quot;a&quot; | &quot;b&quot; | &quot;c&quot; | &quot;d&quot; | &quot;e&quot; | &quot;f&quot;
     *      | &quot;A&quot; | &quot;B&quot; | &quot;C&quot; | &quot;D&quot; | &quot;E&quot; | &quot;F&quot;
     *  </pre>
     *
     *  @param  uuid    The UUID string representation.
     *  @return The UUID from the given String representation.
     *  @throws NullArgumentException   The argument is {@code null}.
     *  @throws EmptyArgumentException  The argument is the empty String.
     *  @throws IllegalArgumentException    The argument is invalid.
     *
     *  @see UUID#fromString(String)
     *  @see UUID#toString()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final UUID uuidFromString( final CharSequence uuid ) throws IllegalArgumentException, EmptyArgumentException, NullArgumentException
    {
        final var retValue = fromString( requireNotEmptyArgument( uuid, "uuid" ).toString() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // uuidFromString()

    /**
     *  Returns a number (more precise, an instance of
     *  {@link BigInteger})
     *  that represents the given UUID.
     *
     *  @param  uuid    The UUID to convert.
     *  @return The number that represents the UUID.
     *
     *  @since 0.1.0
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final BigInteger uuidToNumber( final UUID uuid )
    {
        final var lsb = requireNonNullArgument( uuid, "uuid" ).getLeastSignificantBits();
        final var msb = uuid.getMostSignificantBits();
        var s1 = toBinaryString( lsb );
        s1 = repeat( "0", Long.SIZE - s1.length()) + s1;
        var s2 = toBinaryString( msb );
        s2 = repeat( "0", Long.SIZE - s2.length()) + s2;
        final var retValue = new BigInteger( s2 + s1, 2 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  uuidToNumber()

    /**
     *  Creates a time-based (version type 7) UUID.
     *
     *  @return The UUID.
     */
    @API( status = STABLE, since = "0.1.0" )
    public static final UUID version7UUID()
    {
        final var random = getRandom();

        //---* Calculate the most significant bits *--------------------------
        final long randA;
        final long currentTime;
        synchronized( m_UUID7Counter )
        {
            randA = (long) m_UUID7Counter.getAndIncrement() & 0x0000000000000FFFL;
            if( randA == 0 ) repose( 1 );
            currentTime = currentTimeMillis() << 16;
        }
        final var version = 28672L; //(0x07L << 12) & 0x000000000000F000L;
        final var mostSigBits = currentTime | version | randA;

        //---* Calculate the least significant bits *--------------------------
        final var variant = (0x02L << 62) & 0x8000000000000000L;
        final var randB = (random.nextLong() << 32) & 0x3FFFFFFF00000000L;
        final var randC = random.nextLong() & 0x00000000FFFFFFFFL;
        final var leastSigBits = variant | randB | randC;

        //---* Create the UUID *-----------------------------------------------
        final var retValue = new UUID( mostSigBits, leastSigBits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   // version7UUID()
}
// class UniqueIdUtils

/*
 * End of File
 */