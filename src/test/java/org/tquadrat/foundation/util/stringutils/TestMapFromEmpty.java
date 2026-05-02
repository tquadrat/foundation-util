/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.stringutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.mapFromEmpty;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#mapFromEmpty(CharSequence, CharSequence)}
 *  and
 *  {@link StringUtils#mapFromEmpty(CharSequence, Supplier)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestMapFromEmpty.java 1186 2026-04-06 11:24:14Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestMapFromEmpty.java 1186 2026-04-06 11:24:14Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestMapFromEmpty" )
public class TestMapFromEmpty extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests
     *  {@link StringUtils#mapFromEmpty(CharSequence, CharSequence)}.
     *
     *  @throws Exception   Something went awfully wrong.
     */
    @Test
    final void testMapFromEmptyConstant() throws Exception
    {
        skipThreadTest();

        final var input = "input";
        final var replacement = "replacement";

        assertNull( mapFromEmpty( null, (String) null ) );
        assertNull( mapFromEmpty( EMPTY_CHARSEQUENCE, (String) null ) );
        assertEquals( input, mapFromEmpty( input, (String) null ) );

        assertEquals( replacement, mapFromEmpty( null, replacement ) );
        assertEquals( replacement, mapFromEmpty( EMPTY_CHARSEQUENCE, replacement ) );
        assertEquals( input, mapFromEmpty( input, replacement ) );
    }   //  testMapFromEmptyConstant()

    /**
     *  Tests
     *  {@link StringUtils#mapFromEmpty(CharSequence, Supplier)}
     *
     *  @throws Exception   Something went awfully wrong.
     */
    @Test
    final void testMapFromEmptySupplier() throws Exception
    {
        skipThreadTest();

        final var input = "input";
        final var replacement = "replacement";
        final Supplier<CharSequence> replacementSupplier = () -> replacement;

        assertThrows( NullArgumentException.class, () -> mapFromEmpty( null, (Supplier<? extends CharSequence>) null ) );
        assertThrows( NullArgumentException.class, () -> mapFromEmpty( EMPTY_CHARSEQUENCE, (Supplier<? extends CharSequence>) null ) );
        assertThrows( NullArgumentException.class, () -> mapFromEmpty( input, (Supplier<? extends CharSequence>) null ) );

        assertEquals( replacement, mapFromEmpty( null, replacementSupplier ) );
        assertEquals( replacement, mapFromEmpty( EMPTY_CHARSEQUENCE, replacementSupplier ) );
        assertEquals( input, mapFromEmpty( input, replacementSupplier ) );
    }   //  testMapFromEmptySupplier()
}
//  class TestMapFromEmpty

/*
 *  End of File
 */