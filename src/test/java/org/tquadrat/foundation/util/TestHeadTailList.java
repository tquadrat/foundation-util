/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for the interface
 *  {@link HeadTailList}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.HeadTailListImpl}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestHeadTailList.java 995 2022-01-23 01:09:35Z tquadrat $" )
public class TestHeadTailList extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for
     *  {@link HeadTailList}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testHeadTailList() throws Exception
    {
        skipThreadTest();

        HeadTailList<String> candidate = HeadTailList.empty();
        assertNotNull( candidate );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertTrue( candidate.head().isEmpty() );
        assertTrue( candidate.tail().isEmpty() );

        candidate = candidate.add( "eins" );
        assertNotNull( candidate );
        assertFalse( candidate.isEmpty() );
        assertEquals( 1, candidate.size() );

        assertFalse( candidate.head().isEmpty() );
        assertTrue( candidate.tail().isEmpty() );

        candidate = candidate.add( "zwei" ).add( "drei" ).add( "vier" ).add( "fünf" );
        assertNotNull( candidate );
        assertFalse( candidate.isEmpty() );
        assertEquals( 5, candidate.size() );

        assertFalse( candidate.head().isEmpty() );
        assertFalse( candidate.tail().isEmpty() );

        assertEquals( "eins", candidate.get( 4 ) );
    }   //  testHeadTailList()
}
//  class TestHeadTailList

/*
 *  End of File
 */