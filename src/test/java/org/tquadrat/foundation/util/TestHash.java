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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.util.Hash.create;
import static org.tquadrat.foundation.util.Hash.from;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.internal.HashImpl;

/**
 *  Some tests for
 *  {@link Hash}
 *  and
 *  {@link HashImpl}.
 *
 *  @version $Id: TestHash.java 1045 2023-02-07 23:09:17Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@DisplayName( "org.tquadrat.foundation.util.TestHash" )
public class TestHash extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the argument validation for
     *  {@link Hash#create(File,Checksum)},
     *  {@link Hash#create(Path,Checksum)},
     *  {@link Hash#create(CharSequence,Checksum)},
     *  {@link Hash#create(CharSequence, Charset,Checksum)},
     *  {@link Hash#create(byte [],Checksum)},
     *  {@link Hash#create(File, MessageDigest)},
     *  {@link Hash#create(Path,MessageDigest)},
     *  {@link Hash#create(CharSequence,MessageDigest)},
     *  {@link Hash#create(CharSequence,Charset,MessageDigest)},
     *  {@link Hash#create(byte [],MessageDigest)},
     *  {@link Hash#from(byte[])}
     *  and
     *  {@link Hash#from(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testArgumentValidation() throws Exception
    {
        skipThreadTest();

        final var file = new File( "file" );
        final var path = file.toPath();
        final Checksum checksum = new CRC32();
        final var messageDigest = MessageDigest.getInstance( "MD5" );
        final CharSequence charSequence = "CharSequence";
        final var charset = UTF8;
        final var bytes = new byte [] {1,2,3,4,5,6,7,8,9,0};

        assertThrows( NullArgumentException.class, () -> create( (File) null, checksum ) );
        assertThrows( NullArgumentException.class, () -> create( (Path) null, checksum ) );
        assertThrows( NullArgumentException.class, () -> create( (CharSequence) null, checksum ) );
        assertThrows( NullArgumentException.class, () -> create( (CharSequence) null, charset, checksum ) );
        assertThrows( NullArgumentException.class, () -> create( (byte []) null, checksum ) );

        assertThrows( NullArgumentException.class, () -> create( charSequence, null, checksum ) );

        assertThrows( NullArgumentException.class, () -> create( (File) null, messageDigest ) );
        assertThrows( NullArgumentException.class, () -> create( (Path) null, messageDigest ) );
        assertThrows( NullArgumentException.class, () -> create( (CharSequence) null, messageDigest ) );
        assertThrows( NullArgumentException.class, () -> create( (CharSequence) null, charset, messageDigest ) );
        assertThrows( NullArgumentException.class, () -> create( (byte []) null, messageDigest ) );

        assertThrows( NullArgumentException.class, () -> create( charSequence, null, messageDigest ) );

        assertThrows( NullArgumentException.class, () -> create( file, (Checksum) null ) );
        assertThrows( NullArgumentException.class, () -> create( path, (Checksum) null ) );
        assertThrows( NullArgumentException.class, () -> create( charSequence, (Checksum) null ) );
        assertThrows( NullArgumentException.class, () -> create( charSequence, charset, (Checksum) null ) );
        assertThrows( NullArgumentException.class, () -> create( bytes, (Checksum) null ) );

        assertThrows( NullArgumentException.class, () -> create( file, (MessageDigest) null ) );
        assertThrows( NullArgumentException.class, () -> create( path, (MessageDigest) null ) );
        assertThrows( NullArgumentException.class, () -> create( charSequence, (MessageDigest) null ) );
        assertThrows( NullArgumentException.class, () -> create( charSequence, charset, (MessageDigest) null ) );
        assertThrows( NullArgumentException.class, () -> create( bytes, (MessageDigest) null ) );

        assertThrows( NullArgumentException.class, () -> from( (CharSequence) null ) );
        assertThrows( NullArgumentException.class, () -> from( (byte []) null ) );
    }   //  testArgumentValidation()
}
//  class TestHash

/*
 *  End of File
 */