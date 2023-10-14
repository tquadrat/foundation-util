/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.comparators;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.sort;
import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.util.Comparators.listBasedComparator;
import static org.tquadrat.foundation.util.helper.RandomStringGenerator.generateTestData;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Comparators;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  Tests for the class
 *  {@link Comparators}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestListBasedComparator.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestListBasedComparator.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.comparators.TestListBasedComparator" )
public class TestListBasedComparator extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link Comparators#listBasedComparator(List)},
     *  {@link Comparators#listBasedComparator(KeyProvider, Comparator, Object...)}
     *  and
     *  {@link Comparators#listBasedComparator(KeyProvider, Comparator, List)}.
     *
     *  @see Comparator
     *  @see java.util.Arrays#sort(Object[], Comparator)
     */
    @Test
    final void testListBasedComparator()
    {
        skipThreadTest();

        final var testDataSize = 1000;
        Comparator<String> candidate;
        final Comparator<String> defaultComparator = naturalOrder();
        final var keyProvider = (KeyProvider<String, String>) s -> s;
        final var testData = generateTestData( testDataSize );
        assertEquals( testDataSize, testData.length );
        String [] actual, expected;

        /*
         * We use the unordered testData as the key's list, and the ordered
         * Strings as input. After sorting using the list based comparator, we
         * should get the same order as before.
         *
         * The key provider returns the value, and the default comparator uses
         * the natural order (although that should not be used here).
         */
        expected = testData.clone();
        candidate = listBasedComparator( expected );
        actual = testData.clone();
        sort( actual ); // shuffle
        sort( actual, candidate );
        assertArrayEquals( expected, actual );

        expected = testData.clone();
        candidate = listBasedComparator( keyProvider, defaultComparator, expected );
        actual = testData.clone();
        sort( actual );
        sort( actual, candidate );
        assertArrayEquals( expected, actual );
    }   //  testListBasedComparator()

    /**
     *  Tests for the methods
     *  {@link Comparators#listBasedComparator(Object...)},
     *  {@link Comparators#listBasedComparator(List)},
     *  {@link Comparators#listBasedComparator(KeyProvider, Comparator, Object...)},
     *  and
     *  {@link Comparators#listBasedComparator(KeyProvider, Comparator, List)}.
     */
    @Test
    final void testListBasedComparatorWithNullArgument()
    {
        skipThreadTest();

        final Comparator<String> defaultComparator = naturalOrder();
        final var keyProvider = (KeyProvider<String, String>) s -> s;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        try
        {
            listBasedComparator( (String []) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            listBasedComparator( null, defaultComparator, EMPTY_String_ARRAY );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            listBasedComparator( keyProvider, defaultComparator, (String []) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testListBasedComparatorWithNullArgument()
}
//  class TestListBasedComparator

/*
 *  End of File
 */