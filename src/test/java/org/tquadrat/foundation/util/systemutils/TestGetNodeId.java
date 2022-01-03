/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.util.systemutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.tquadrat.foundation.util.SystemUtils.getNodeId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SystemUtils;

/**
 *  Some tests for the method
 *  {@link SystemUtils#getNodeId()}.
 *  from the class
 *  {@link SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestGetNodeId.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestGetNodeId.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.systemutils.TestGetNodeId" )
public class TestGetNodeId extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the retrieval of a node id (the numerical representation of a MAC
     *  address).
     *
     *  @see SystemUtils#getNodeId()
     */
    @Test
    final void testGetNodeId()
    {
        skipThreadTest();

        assumeTrue( hasNetwork() );

        final var first = getNodeId();
        assertTrue( Long.MIN_VALUE != first );
        assertTrue( first != 0L );

        /*
         * While the program is running, any consecutive call to getNodeId()
         * have to return the same value.
         */
        for( var i = 0; i < 100; ++i )
        {
            assertEquals( first, getNodeId() );
        }
    }   //  testGetNodeId()
}
//  class TestGetNodeId

/*
 *  End of File
 */