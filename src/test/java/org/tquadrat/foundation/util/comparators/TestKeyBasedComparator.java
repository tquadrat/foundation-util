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
import static org.tquadrat.foundation.util.Comparators.keyBasedComparator;
import static org.tquadrat.foundation.util.helper.RandomStringGenerator.generateTestData;

import java.util.Comparator;

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
 *  @version $Id: TestKeyBasedComparator.java 1076 2023-10-03 18:36:07Z tquadrat $
 *  @since Java 10
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestKeyBasedComparator.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.comparators.TestKeyBasedComparator" )
public class TestKeyBasedComparator extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link Comparators#keyBasedComparator(KeyProvider)}
     *  and
     *  {@link Comparators#keyBasedComparator(KeyProvider, Comparator, boolean)}.
     */
    @Test
    final void testKeyBasedComparator()
    {
        skipThreadTest();

        final var testDataSize = 1000;
        Comparator<String> candidate;
        final Comparator<String> keyComparator = naturalOrder();
        final KeyProvider<String,String> keyProvider = s -> s;
        final var testData = generateTestData( testDataSize );
        assertEquals( testDataSize, testData.length );
        String [] actual, expected;

        /*
         * As we use a key provider that returns the value, and a key
         * comparator that uses the natural order of the keys, the result from
         * the key based comparator should not be different from a sort without
         * any comparator.
         */
        candidate = keyBasedComparator( keyProvider );
        expected = testData.clone();
        sort( expected );
        actual = testData.clone();
        sort( actual, candidate );
        assertArrayEquals( expected, actual );

        candidate = keyBasedComparator( keyProvider, keyComparator, true );
        expected = testData.clone();
        sort( expected );
        actual = testData.clone();
        sort( actual, candidate );
        assertArrayEquals( expected, actual );

        candidate = keyBasedComparator( keyProvider, keyComparator, false );
        expected = testData.clone();
        sort( expected );
        actual = testData.clone();
        sort( actual, candidate );
        assertArrayEquals( expected, actual );
    }   //  testKeyBasedComparator()

    /**
     *  Tests for the methods
     *  {@link Comparators#keyBasedComparator(KeyProvider)}
     *  and
     *  {@link Comparators#keyBasedComparator(KeyProvider, Comparator, boolean)}.
     */
    @Test
    final void testKeyBasedComparatorWithNullArgument()
    {
        skipThreadTest();

        final Comparator<String> keyComparator = naturalOrder();
        final KeyProvider<String,String> keyProvider = s -> s;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        try
        {
            keyBasedComparator( null );
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
            keyBasedComparator( null, keyComparator, false );
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
            keyBasedComparator( keyProvider, null, false );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testKeyBasedComparatorWithNullArgument()
}
//  class TestKeyBasedComparator

/*
 *  End of File
 */