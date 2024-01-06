/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.util;

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.IOUtils.determineCheckSum;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.ImpossibleExceptionError;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  This class provides some utility functions that are helpful in the
 *  security arena. <br>
 *  <br>The methods are thread safe, but they use a global message digest. As
 *  a consequence, multiple threads that are calculating hashes will serialise
 *  on the use of those digest. That is acceptable for an application where
 *  this calculation does not occur that often (for example a web application
 *  that needs to check a password at login) but not for an application that
 *  uses multiple threads to calculate the hashes for a bunch of files.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SecurityUtils.java 1086 2024-01-05 23:18:33Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: SecurityUtils.java 1086 2024-01-05 23:18:33Z tquadrat $" )
@UtilityClass
public final class SecurityUtils
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  A recommended prime for the Diffie-Hellman-Merkle key exchange scheme.
     *
     *  @see #calculateDiffieHellmanEncryptionKey(BigInteger,BigInteger,BigInteger)
     *  @see #calculateDiffieHellmanPublicValue(BigInteger,BigInteger,BigInteger)
     *  @see <a href="http://tools.ietf.org/html/rfc2412#page-45">http://tools.ietf.org/html/rfc2412#page-45</a>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final BigInteger DHM_PRIME = new BigInteger
    (
        """
        2410312426921032588552076022197566074856950548502459942654116941958\
        1088316826122288900938582613416146732271414779040121965036489570505\
        8263194273070680500922306273474534107340669624601458936165977404102\
        7169249453200378729434170325843778659198143763193776859869524088940\
        1955773461198435453015470437472077499697637500843089263392955599688\
        8245787241299381012913029459299994792636526405928464720973038494721\
        1681434464714438488520940127459844288859336526896320919633919""" );

    /**
     *  A recommended prime modulus (primitive root) for the
     *  Diffie-Hellman-Merkle key exchange scheme.
     *
     *  @see #calculateDiffieHellmanEncryptionKey(BigInteger,BigInteger,BigInteger)
     *  @see #calculateDiffieHellmanPublicValue(BigInteger,BigInteger,BigInteger)
     *  @see <a href="http://tools.ietf.org/html/rfc2412#page-45">http://tools.ietf.org/html/rfc2412#page-45</a>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final BigInteger DHM_PRIME_MOD = BigInteger.valueOf( 21 );

    /**
     *  The message that indicates that the named algorithm is not supported:
     *  {@value}.
     */
    private static final String MSG_AlgorithmNotSupported = "MessageDigest does not support '%1$s' Algorithm";

    /**
     *  The length for an MD5 hash: {@value}.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final int MD5HASH_Length = 32;

    /**
     *  The length for an SHA1 hash: {@value}.
     */
    @API( status = STABLE, since = "0.0.8" )
    public static final int SHA1HASH_Length = 40;

    /**
     *  The length for an SHA256 hash: {@value}.
     */
    @API( status = STABLE, since = "0.0.8" )
    public static final int SHA256HASH_Length = 64;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The message digest that is used to encrypt the passwords with the MD5
     *  hash algorithm.
     */
    private static final MessageDigest m_MD5MessageDigest;

    /**
     *  The message digest that is used to encrypt the passwords with the SHA-1
     *  hash algorithm.
     */
    private static final MessageDigest m_SHA1MessageDigest;

    /**
     *  The message digest that is used to encrypt the passwords with the SHA-1
     *  hash algorithm.
     */
    private static final MessageDigest m_SHA256MessageDigest;

    static
    {
        //---* Create the MD5 digest *-----------------------------------------
        var algorithm = "MD5";
        try
        {
            m_MD5MessageDigest = MessageDigest.getInstance( algorithm );
        }
        catch( final NoSuchAlgorithmException e )
        {
            throw new ImpossibleExceptionError( format( MSG_AlgorithmNotSupported, algorithm ), e );
        }

        //---* Create the SHA digest *-----------------------------------------
        algorithm = "SHA";
        try
        {
            m_SHA1MessageDigest = MessageDigest.getInstance( algorithm );
        }
        catch( final NoSuchAlgorithmException e )
        {
            throw new ImpossibleExceptionError( format( MSG_AlgorithmNotSupported, algorithm ), e );
        }

        //---* Create the SHA-256 digest *-------------------------------------
        algorithm = "SHA-256";
        try
        {
            m_SHA256MessageDigest = MessageDigest.getInstance( algorithm );
        }
        catch( final NoSuchAlgorithmException e )
        {
            throw new ImpossibleExceptionError( format( MSG_AlgorithmNotSupported, algorithm ), e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instances are allowed for this class.
     */
    private SecurityUtils() { throw new PrivateConstructorForStaticClassCalledError( SecurityUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Performs the calculation for the Diffie-Hellmann-Merkle
     *  key exchange procedure.}</p>
     *  <p>From Wikipedia:</p> <blockquote><p>Diffie–Hellman establishes a shared
     *  secret that can be used for secret communications while exchanging data
     *  over a public network. Diffie–Hellman key exchange (D-H) is a specific
     *  method of exchanging cryptographic keys. It is one of the earliest
     *  practical examples of key exchange implemented within the field of
     *  cryptography. The Diffie–Hellman key exchange method allows two parties
     *  that have no prior knowledge of each other to jointly establish a
     *  shared secret key over an insecure communications channel. This key can
     *  then be used to encrypt subsequent communications using a symmetric key
     *  cipher.</p>
     *  <p>The scheme was first published by Whitfield Diffie and Martin
     *  Hellman in 1976, although it had been separately invented a few years
     *  earlier within GCHQ, the British signals intelligence agency, by James
     *  H. Ellis, Clifford Cocks and Malcolm J. Williamson but was kept
     *  classified. In 2002, Hellman suggested the algorithm be called
     *  Diffie–Hellman–Merkle key exchange in recognition of Ralph Merkle's
     *  contribution to the invention of public-key cryptography (Hellman,
     *  2002).</p>
     *  <p>Although Diffie–Hellman key agreement itself is an anonymous
     *  (non-authenticated) key-agreement protocol, it provides the basis for a
     *  variety of authenticated protocols, and is used to provide <i>perfect
     *  forward secrecy</i> in Transport Layer Security's ephemeral modes
     *  (referred to as EDH or DHE depending on the cipher
     *  suite).</p></blockquote>
     *  <p>This method performs the following calculation:</p>
     *  <pre><code>K = remoteSecret<sup>localSecret</sup> mod prime</code></pre>
     *
     *  @param  prime   A large prime that is known to both communication
     *      partners.
     *  @param  localSecret The secret number that was used to create the
     *      public value sent to the other party.
     *  @param  remoteSecret    The public value that the other party created.
     *  @return The encryption key <i>K</i>.
     *
     *  @see <a href="https://en.wikipedia.org/wiki/Diffie%E2%80%93Hellman_key_exchange">Wikipedia: Diffie–Hellman key exchange</a>
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final BigInteger calculateDiffieHellmanEncryptionKey( final BigInteger prime, final BigInteger localSecret, final BigInteger remoteSecret )
    {
        final var retValue = requireNonNullArgument( remoteSecret, "remoteSecret" )
            .modPow( requireNonNullArgument( localSecret, "localSecret" ), requireNonNullArgument( prime, "prime" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateDiffieHellmanEncryptionKey()

    /**
     *  <p>{@summary Calculates the value that will be transmitted to the other
     *  party on the exchange of an encryption key using the
     *  Diffie-Hellman-Merkle key exchange scheme.}</p>>
     *  <p>This method performs the following calculation:</p>
     *  <pre><code>A = root<sup>random</sup> mod prime</code></pre>.
     *
     *  @param  prime   A large prime that is known to both communication partners.
     *  @param  root    A primitive root <i>mod {@code prime}</i>.
     *  @param  localSecret The secret random number.
     *  @return The value <i>A</i> to transmit to the other party.
     *
     *  @see #calculateDiffieHellmanEncryptionKey(BigInteger,BigInteger,BigInteger)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final BigInteger calculateDiffieHellmanPublicValue( final BigInteger prime, final BigInteger root, final BigInteger localSecret )
    {
        final var retValue = requireNonNullArgument( root, "root" )
            .modPow( requireNonNullArgument( localSecret, "localSecret" ), requireNonNullArgument( prime, "prime" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateDiffieHellmanPublicValue()

    /**
     *  Calculates a checksum for the given file, based on the MD5 algorithm.
     *
     *  @param  file    The file.
     *  @return The check sum.
     *  @throws IOException Something went wrong on reading the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String calculateMD5CheckSum( final File file ) throws IOException
    {
        requireNonNullArgument( file, "file" );

        String retValue = null;
        synchronized( m_MD5MessageDigest )
        {
            final var hash = determineCheckSum( file, m_MD5MessageDigest );
            retValue = HexFormat.of().withUpperCase().formatHex( hash );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateMD5CheckSum()

    /**
     *  Calculates a checksum for the given file, based on the SHA-1 algorithm.
     *
     *  @param  file    The file.
     *  @return The check sum.
     *  @throws IOException Something went wrong on reading the file.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String calculateSHACheckSum( final File file ) throws IOException
    {
        requireNonNullArgument( file, "file" );

        String retValue = null;
        synchronized( m_SHA1MessageDigest )
        {
            final var hash = determineCheckSum( file, m_SHA1MessageDigest );
            retValue = HexFormat.of().withUpperCase().formatHex( hash );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHACheckSum()

    /**
     *  Calculates a checksum for the given file, based on the SHA-256
     *  algorithm.
     *
     *  @param  file    The file.
     *  @return The check sum.
     *  @throws IOException Something went wrong on reading the file.
     */
    @API( status = STABLE, since = "0.0.8" )
    public static final String calculateSHA256CheckSum( final File file ) throws IOException
    {
        requireNonNullArgument( file, "file" );

        String retValue = null;
        synchronized( m_SHA256MessageDigest )
        {
            final var hash = determineCheckSum( file, m_SHA256MessageDigest );
            retValue = HexFormat.of().withUpperCase().formatHex( hash );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHA256CheckSum()

    /**
     *  Creates a MD5 hash from the given string.<br>
     *  <br>The output string will contain the digits from {@code 0xA} to
     *  {@code 0xF} all as lower
     *  case.<br>
     *  <br>Use this method to create the values for password fields; it is
     *  not very efficient for calculating the hash value for (large) files as
     *  it would require to load the whole file into memory.
     *
     *  @param  input   The source String; may be {@code null}.
     *  @return The String with the hash value or {@code null} if the
     *      input parameter was already {@code null}.
     *
     *  @see #calculateMD5CheckSum(File)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String calculateMD5Hash( final CharSequence input )
    {
        String retValue = null;
        if( nonNull( input ) )
        {
            final var inputBytes = input.toString().getBytes( UTF8 );
            retValue = HexFormat.of().withUpperCase().formatHex( calculateMD5Hash( inputBytes ) ).toLowerCase( Locale.ROOT );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateMD5Hash()

    /**
     *  Creates a MD5 hash from the given byte sequence.<br>
     *  <br> This method is not very efficient for calculating the hash value
     *  for (large) files as it would require to load the whole file into
     *  memory.
     *
     *  @param  input   The byte array to hash.
     *  @return The byte array with the hash.
     *
     *  @see #calculateMD5CheckSum(File)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final byte [] calculateMD5Hash( final byte [] input )
    {
        requireNonNullArgument( input, "input" );

        byte [] retValue = null;
        synchronized( m_MD5MessageDigest )
        {
            m_MD5MessageDigest.reset();
            m_MD5MessageDigest.update( input );
            retValue = m_MD5MessageDigest.digest();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateMD5Hash()

    /**
     *  Creates an SHA-1 hash from the given string.<br>
     *  <br>The output string will contain the digits from {@code 0xA} to
     *  {@code 0xF} all as lower
     *  case.<br>
     *  <br>Use this method to create the values for password fields; it is
     *  not very efficient for calculating the hash value for (large) files as
     *  it would require to load the whole file into memory.
     *
     *  @param  input   The source String; may be {@code null}.
     *  @return The String with the hash value or {@code null} if the
     *      input parameter was already {@code null}.
     *
     *  @see #calculateSHACheckSum(File)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String calculateSHA1Hash( final CharSequence input )
    {
        String retValue = null;
        if( nonNull( input ) )
        {
            final var inputBytes = input.toString().getBytes( UTF8 );
            retValue = HexFormat.of().withLowerCase().formatHex( calculateSHA1Hash( inputBytes ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHA1Hash()

    /**
     *  Creates an SHA-1 hash from the given byte sequence.<br>
     *  <br> This method is not very efficient for calculating the hash value
     *  for (large) files as it would require to load the whole file into
     *  memory.
     *
     *  @param  input   The byte array to hash.
     *  @return The byte array with the hash.
     *
     *  @see #calculateSHACheckSum(File)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final byte [] calculateSHA1Hash( final byte [] input )
    {
        requireNonNullArgument( input, "input" );

        byte [] retValue = null;
        synchronized( m_SHA1MessageDigest )
        {
            m_SHA1MessageDigest.reset();
            m_SHA1MessageDigest.update( input );
            retValue = m_SHA1MessageDigest.digest();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHA1Hash()

    /**
     *  Creates an SHA-256 hash from the given string.<br>
     *  <br>The output string will contain the digits from {@code 0xA} to
     *  {@code 0xF} all as lower
     *  case.<br>
     *  <br>Use this method to create the values for password fields; it is
     *  not very efficient for calculating the hash value for (large) files as
     *  it would require to load the whole file into memory.
     *
     *  @param  input   The source String; may be {@code null}.
     *  @return The String with the hash value or {@code null} if the
     *      input parameter was already {@code null}.
     *
     *  @see #calculateSHA256CheckSum(File)
     */
    @API( status = STABLE, since = "0.0.8" )
    public static final String calculateSHA256Hash( final CharSequence input )
    {
        String retValue = null;
        if( nonNull( input ) )
        {
            final var inputBytes = input.toString().getBytes( UTF8 );
            retValue = HexFormat.of().withLowerCase().formatHex( calculateSHA256Hash( inputBytes ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHA256Hash()

    /**
     *  Creates an SHA-256 hash from the given byte sequence.<br>
     *  <br> This method is not very efficient for calculating the hash value
     *  for (large) files as it would require to load the whole file into
     *  memory.
     *
     *  @param  input   The byte array to hash.
     *  @return The byte array with the hash.
     *
     *  @see #calculateSHA256CheckSum(File)
     */
    @API( status = STABLE, since = "0.0.8" )
    public static final byte [] calculateSHA256Hash( final byte [] input )
    {
        requireNonNullArgument( input, "input" );

        byte [] retValue = null;
        synchronized( m_SHA256MessageDigest )
        {
            m_SHA256MessageDigest.reset();
            m_SHA256MessageDigest.update( input );
            retValue = m_SHA256MessageDigest.digest();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  calculateSHA256Hash()
}
//  class SecurityUtils()

/*
 *  End of File
 */