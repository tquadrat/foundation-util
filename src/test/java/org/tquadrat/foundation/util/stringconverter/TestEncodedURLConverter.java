/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
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

package org.tquadrat.foundation.util.stringconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.ASCII;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;

/**
 *  Tests for the class
 *  {@link EncodedURLStringConverter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestEncodedURLConverter.java 1004 2022-02-02 11:23:44Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestEncodedURLConverter.java 1004 2022-02-02 11:23:44Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringconverter.TestEncodedURLConverter" )
public class TestEncodedURLConverter extends TestURLConverter
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link EncodedURLStringConverter#toString(URL, Charset)}
     *  and
     *  {@link EncodedURLStringConverter#fromString(CharSequence, Charset)}.
     */
    @Test
    final void testNullArgument()
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> new EncodedURLStringConverter( null ) );

        final var candidate = new EncodedURLStringConverter( ASCII );
        assertNotNull( candidate );

        assertThrows( NullArgumentException.class, () -> candidate.fromString( null, null ) );
        assertThrows( NullArgumentException.class, () -> candidate.toString( null, null ) );
    }   //  testNullArgument()

    /**
     *  The tests for
     *  {@link EncodedURLStringConverter}.
     */
    @Override
    @Test
    final void testURLConversion()
    {
        skipThreadTest();

        final var candidate = EncodedURLStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( EMPTY_STRING ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( " " ) );
        assertThrows( IllegalArgumentException.class, () -> candidate.fromString( "Fußpilz" ) );
    }   //  testURLConversion()

    /**
     *  The tests for
     *  {@link EncodedURLStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @Override
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final URL value )
    {
        skipThreadTest();

        final var candidate = EncodedURLStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
    }   //  testValueConversion()
}
//  class TestEncodedURLConverter

/*
 *  End of File
 */