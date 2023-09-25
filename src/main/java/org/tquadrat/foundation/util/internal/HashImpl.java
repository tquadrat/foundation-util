/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.internal;

import static java.nio.file.StandardOpenOption.READ;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotBlankArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.HexUtils.convertFromHexString;
import static org.tquadrat.foundation.util.IOUtils.DEFAULT_BUFFER_SIZE;

import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.zip.Checksum;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.util.Hash;

/**
 *  The implementation for the interface
 *  {@link Hash}.
 *
 *  @version $Id: HashImpl.java 1060 2023-09-24 19:21:40Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.1
 */
@ClassVersion( sourceVersion = "$Id: HashImpl.java 1060 2023-09-24 19:21:40Z tquadrat $" )
@API( status = INTERNAL, since = "0.1.1" )
public final class HashImpl implements Hash
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The hash value.
     */
    private final byte [] m_HashValue;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 539879857L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code HashImpl}.
     *
     *  @param  hashValue   The hash value.
     */
    public HashImpl( final byte [] hashValue )
    {
        m_HashValue = requireNotEmptyArgument( hashValue, "hashValue" );
    }   //  HashImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final byte [] bytes() { return m_HashValue.clone(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final HashImpl clone()
    {
        final HashImpl retValue;
        try
        {
            retValue = (HashImpl) super.clone();
        }
        catch( final CloneNotSupportedException e )
        {
            throw new UnexpectedExceptionError( e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  clone()

    /**
     *  Creates the hash for the given byte array, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code HashImpl}.
     */
    public static final HashImpl create( final byte [] data, final Checksum algorithm )
    {
        requireNonNullArgument( algorithm, "algorithm" ).reset();
        algorithm.update( requireNonNullArgument( data, "data" ) );
        final var hashValue = algorithm.getValue();
        final var retValue = from( Long.toHexString( hashValue ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code HashImpl}.
     *  @throws IOException Problems to process the file.
     */
    public static Hash create( final Path data, final Checksum algorithm ) throws IOException
    {
        requireNonNullArgument( algorithm, "algorithm" ).reset();
        try( final var inputStream = Files.newInputStream( requireNonNullArgument( data, "data" ), READ ) )
        {
            final var buffer = new byte [DEFAULT_BUFFER_SIZE];
            var readBytes = 0;
            //noinspection NestedAssignment
            while( (readBytes = inputStream.read( buffer )) > 0 )
            {
                algorithm.update( buffer, 0, readBytes );
            }
        }
        final var retValue = from( Long.toHexString( algorithm.getValue() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  create()

    /**
     *  Creates the hash for the given byte array, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code HashImpl}.
     */
    public static final HashImpl create( final byte [] data, final MessageDigest algorithm )
    {
        requireNonNullArgument( algorithm, "algorithm" ).reset();
        final var hashValue = algorithm.digest( requireNonNullArgument( data, "data" ) );
        final var retValue = new HashImpl( hashValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @throws IOException Problems to process the file.
     *  @return A new instance of {@code HashImpl}.
     */
    public static Hash create( final Path data, final MessageDigest algorithm ) throws IOException
    {
        requireNonNullArgument( algorithm, "algorithm" ).reset();
        try( final var inputStream = Files.newInputStream( requireNonNullArgument( data, "data" ), READ ) )
        {
            final var buffer = new byte [DEFAULT_BUFFER_SIZE];
            var readBytes = 0;
            //noinspection NestedAssignment
            while( (readBytes = inputStream.read( buffer )) > 0 )
            {
                algorithm.update( buffer, 0, readBytes );
            }
        }
        final var retValue = new HashImpl( algorithm.digest() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  create()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object o )
    {
        var retValue = this == o;
        if( !retValue && o instanceof final HashImpl other )
        {
            retValue = Arrays.equals( m_HashValue, other.m_HashValue );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  Creates an instance of {@code Hash} from the given String.
     *
     *  @param  hashValue   The hash value.
     *  @return A new instance of {@code HashImpl}.
     */
    public static final HashImpl from( final CharSequence hashValue )
    {
        final var retValue = new HashImpl( convertFromHexString( requireNotBlankArgument( hashValue, "hashValue" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  from()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return Arrays.hashCode( m_HashValue ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return HexFormat.of().formatHex( m_HashValue ); }
}
//  class HashImpl

/*
 *  End of File
 */