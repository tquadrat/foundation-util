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

package org.tquadrat.foundation.util;

import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Checksum;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.HashImpl;

/**
 *  <p>{@summary The definition for a wrapper around hash values of any kind.}
 *  These hashes are often used as checksums to validate the integrity of files
 *  or messages.</p>
 *
 *  @version $Id: Hash.java 1052 2023-03-06 06:30:36Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.1
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: Hash.java 1052 2023-03-06 06:30:36Z tquadrat $" )
@API( status = STABLE, since = "0.1.1" )
public sealed interface Hash extends Cloneable, Serializable
    permits HashImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the hash as an array of bytes.
     *
     *  @return The hash value.
     */
    public byte [] bytes();

    /**
     *  Creates and returns a copy of this object.
     *
     *  @return The copy of this instance.
     */
    public Hash clone();

    /**
     *  Creates the hash for the given byte array, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final byte [] data, final Checksum algorithm )
    {
        return HashImpl.create( data, algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given String, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final CharSequence data, final Checksum algorithm )
    {
        return create( data, UTF8, algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given String, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  encoding    The encoding for the String.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final CharSequence data, final Charset encoding, final Checksum algorithm )
    {
        return HashImpl.create( requireNonNullArgument( data, "data" ).toString().getBytes( requireNonNullArgument( encoding, "encoding" ) ), algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     *  @throws IOException Problems to process the file.
     */
    public static Hash create( final File data, final Checksum algorithm ) throws IOException
    {
        return create( requireNonNullArgument( data, "data" ).toPath(), algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     *  @throws IOException Problems to process the file.
     */
    public static Hash create( final Path data, final Checksum algorithm ) throws IOException
    {
        return HashImpl.create( data, algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given byte array, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final byte [] data, final MessageDigest algorithm )
    {
        return HashImpl.create( data, algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given String, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final CharSequence data, final MessageDigest algorithm )
    {
        return create( data, UTF8, algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given String, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  encoding    The encoding for the String.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final CharSequence data, final Charset encoding, final MessageDigest algorithm )
    {
        return HashImpl.create( requireNonNullArgument( data, "data" ).toString().getBytes( requireNonNullArgument( encoding, "encoding" ) ), algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @throws IOException Problems to process the file.
     *  @return A new instance of {@code Hash}.
     */
    public static Hash create( final File data, final MessageDigest algorithm ) throws IOException
    {
        return create( requireNonNullArgument( data, "data" ).toPath(), algorithm );
    }   //  create()

    /**
     *  Creates the hash for the given file, using the given algorithm.
     *
     *  @param  data    The input data.
     *  @param  algorithm   The algorithm
     *  @return A new instance of {@code Hash}.
     *  @throws IOException Problems to process the file.
     */
    public static Hash create( final Path data, final MessageDigest algorithm ) throws IOException
    {
        return HashImpl.create( data, algorithm );
    }   //  create()

    /**
     *  Creates an instance of {@code Hash} from the given byte array.
     *
     *  @param  hashValue   The hash value.
     *  @return A new instance of {@code Hash}.
     */
    public static Hash from( final byte [] hashValue ) { return new HashImpl( hashValue ); }

    /**
     *  Creates an instance of {@code Hash} from the given String.
     *
     *  @param  hashValue   The hash value.
     *  @return A new instance of {@code Hash}.
     */
    public static Hash from( final CharSequence hashValue ) { return HashImpl.from( hashValue ); }

    /**
     *  Returns this hash as a number.
     *
     *  @return The number.
     */
    public default BigInteger number() { return new BigInteger( bytes() ); }

    /**
     *  Validates whether the given hash value matches with this hash instance.
     *
     *  @param  hashValue   The hash value to test.
     *  @return {@code true} if the hash value matches with this hash instance,
     *      {@code false} otherwise.
     */
    public default boolean validate( final byte [] hashValue ) { return Arrays.equals( bytes(), hashValue ); }

    /**
     *  Validates whether the given hash value matches with this hash instance.
     *
     *  @param  hashValue   The hash value to test.
     *  @return {@code true} if the hash value matches with this hash instance,
     *      {@code false} otherwise.
     */
    public default boolean validate( final long hashValue ) { return number().longValue() == hashValue; }

    /**
     *  Validates whether the given hash value matches with this hash instance.
     *
     *  @param  hashValue   The hash value to test.
     *  @return {@code true} if the hash value matches with this hash instance,
     *      {@code false} otherwise.
     */
    public default boolean validate( final CharSequence hashValue )
    {
        return toString().toLowerCase( ROOT )
            .equals( requireNonNullArgument( hashValue, "hashValue" ).toString().toLowerCase( ROOT ) );
    }   //  validate()
}
//  interface Hash

/*
 *  End of File
 */