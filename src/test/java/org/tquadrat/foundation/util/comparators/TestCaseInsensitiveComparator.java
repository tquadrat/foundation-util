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

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Arrays.sort;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.Comparators.caseInsensitiveComparator;
import static org.tquadrat.foundation.util.helper.RandomStringGenerator.generateTestData;

import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Comparators;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Tests for the class
 *  {@link Comparators}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCaseInsensitiveComparator.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestCaseInsensitiveComparator.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.comparators.TestCaseInsensitiveComparator" )
public class TestCaseInsensitiveComparator extends TestBaseClass
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
    final void testCaseInsensitiveComparator()
    {
        skipThreadTest();

        final var testDataSize = 1000;
        final var candidate = caseInsensitiveComparator();
        final var testData = generateTestData( testDataSize );
        assertEquals( testDataSize, testData.length );

        /*
         * The case insensitive comparator will return
         * String.CASE_INSENSITIVE_ORDER, therefore the results should be the
         * same.
         */
        final var expected = testData.clone();
        assertEquals( testDataSize, expected.length );
        assertTrue( stream( expected ).allMatch( StringUtils::isNotEmptyOrBlank ) );
        sort( expected, CASE_INSENSITIVE_ORDER );
        final var actual = testData.clone();
        sort( actual, candidate );
        assertArrayEquals( expected, actual );
    }   //  testCaseInsensitiveComparator()
}
//  class TestCaseInsensitiveComparator

/*
 *  End of File
 */