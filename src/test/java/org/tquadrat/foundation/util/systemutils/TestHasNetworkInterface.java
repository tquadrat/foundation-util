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
import static org.tquadrat.foundation.util.SystemUtils.hasNetworkInterface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SystemUtils;

/**
 *  Some tests for the method
 *  {@link SystemUtils#hasNetworkInterface()}.
 *  from the class
 *  {@link SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestHasNetworkInterface.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestHasNetworkInterface.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.systemutils.TestHasNetworkInterface" )
public class TestHasNetworkInterface extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test of the method
     *  {@link SystemUtils#hasNetworkInterface()}.
     */
    @Test
    final void testHasNetworkInterface()
    {
        skipThreadTest();

        assertEquals( hasNetwork(), hasNetworkInterface() );
    }   //  testHasNetworkInterface()
}
//  class TestHasNetworkInterface

/*
 *  End of File
 */