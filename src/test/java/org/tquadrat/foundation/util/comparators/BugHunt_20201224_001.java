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

import static java.util.Arrays.sort;
import static java.util.Collections.unmodifiableMap;
import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.tquadrat.foundation.util.Comparators.listBasedComparator;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Comparators;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  The SVG elements should sort the attribute '{@code id}' always to the head
 *  of the list of attributes, but this does not work.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: BugHunt_20201224_001.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: BugHunt_20201224_001.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.comparators.BugHunt_20201224_001" )
public class BugHunt_20201224_001 extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link Comparators#listBasedComparator(KeyProvider, Comparator, Object...)}.
     *
     *  @see Comparator
     *  @see java.util.Arrays#sort(Object[], Comparator)
     */
    @Test
    final void testListBasedComparator()
    {
        skipThreadTest();

        final var keys = new String [] {"id", "viewBox", "externalResourcesRequired"};
        final Comparator<String> candidate = listBasedComparator( s -> s, naturalOrder(), keys );

        final var expected = keys.clone();
        assertEquals( expected [0], keys [0] );
        final var input = keys.clone();
        sort( input );
        assertNotEquals( expected, input );
        sort( input, candidate );
        assertEquals( expected [0], input [0] );
        assertArrayEquals( expected, input );

        final Map<String,String> map = new TreeMap<>( candidate );
        for( final var s : expected ) map.put( s, s );
        final var actual = unmodifiableMap( map ).keySet().toArray( String[]::new );
        assertEquals( expected [0], actual [0] );
        assertArrayEquals( expected, actual );
    }   //  testListBasedComparator()
}
//  class BugHunt_20201224_001

/*
 *  End of File
 */