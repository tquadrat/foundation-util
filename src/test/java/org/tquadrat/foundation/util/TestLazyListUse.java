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

package org.tquadrat.foundation.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazyList}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazyListImpl}.<br>
 *  <br>In particular, we test with a supplier that creates not only the list,
 *  but will add contents to it as well.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazyListUse.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestLazyListUse.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestLazyListUse" )
public class TestLazyListUse extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates the test candidate.
     *
     *  @return The candidate.
     */
    private static final LazyList<String> createCandidate()
    {
        final var retValue = LazyList.use( true, () ->
        {
            final List<String> list = new ArrayList<>();
            list.add( "presetValue1" );
            list.add( "presetValue2" );
            list.add( "presetValue3" );
            return list;
        } );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  Tests for the methods in
     *  {@link LazyList}.
     */
    @SuppressWarnings( "cast" )
    @DisplayName( "LazyList methods" )
    @Test
    final void testMethods()
    {
        skipThreadTest();

        LazyList<String> candidate;

        candidate = createCandidate();
        assertNotNull( candidate );
        assertTrue( candidate instanceof List );
        assertFalse( candidate.isPresent() );
        assertEquals( "[presetValue1, presetValue2, presetValue3]", candidate.toString() );

        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        candidate = createCandidate();
        assertNotNull( candidate );
        assertTrue( candidate instanceof List );
        assertFalse( candidate.isPresent() );

        assertFalse( candidate.contains( "value" ) );
        assertTrue( candidate.isPresent() );

        candidate = createCandidate();
        assertNotNull( candidate );
        assertTrue( candidate instanceof List );
        assertFalse( candidate.isPresent() );

        assertNotNull( candidate.get( 0 ) );
        assertTrue( candidate.isPresent() );
    }   //  testMethods()
}
//  class TestLazyListUse

/*
 *  End of File
 */