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

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;
import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tquadrat.foundation.util.Comparators.numberComparator;
import static org.tquadrat.foundation.util.helper.RandomStringGenerator.provideStrings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Comparators;

/**
 *  Tests for the class
 *  {@link Comparators}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestNumberComparator.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestNumberComparator.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.comparators.TestNumberComparator" )
public class TestNumberComparator extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link Comparators#caseInsensitiveComparator()}.
     *
     *  @see Comparator
     *  @see java.util.Arrays#sort(Object[], Comparator)
     */
    @Test
    final void testNumberComparator()
    {
        skipThreadTest();

        final var candidate = numberComparator();
        final var testData = provideStrings().map( String::hashCode ).toArray( Integer []::new );

        /*
         * The natural order for numbers is obvious, so the results should be
         * the same, with and without comparator.
         */
        final var expected = testData.clone();
        sort( expected );
        final var actual = testData.clone();
        sort( actual, candidate );
        assertArrayEquals( expected, actual );

        final List<Integer> testList = new ArrayList<>( asList( testData ) );
        testList.add( Integer.MAX_VALUE );
        testList.add( Integer.MIN_VALUE );
        testList.add( Integer.valueOf( 0 ) );

        final List<Integer> actualList = new ArrayList<>( testList);
        testList.sort( naturalOrder());
        actualList.sort( candidate );
        assertEquals( testList, actualList );
    }   //  testNumberComparator()
}
//  class TestNumberComparator

/*
 *  End of File
 */