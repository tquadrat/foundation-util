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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.tquadrat.foundation.util.SystemUtils.determineOutboundIPAddresses;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SystemUtils;

/**
 *  Some tests for the method
 *  {@link SystemUtils#determineOutboundIPAddresses()}.
 *  from the class
 *  {@link SystemUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDetermineOutboundIPAddresses.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestDetermineOutboundIPAddresses.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.systemutils.TestDetermineOutboundIPAddresses" )
public class TestDetermineOutboundIPAddresses extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link SystemUtils#determineOutboundIPAddresses()}.
     */
    @Test
    final void testDetermineOutboundIPAddresses()
    {
        skipThreadTest();

        assumeTrue( hasNetwork() );

        final var inetAddresses = determineOutboundIPAddresses();
        assertNotNull( inetAddresses );
        /*
         * The returned array may be empty if the machine does have network,
         * but only the loopback device is active.
         * But the return value is not allowed to be null, nor an exception may
         * be thrown.
         */
    }   //  testDetermineOutboundIPAddresses()
}
//  class TestDetermineOutboundIPAddresses

/*
 *  End of File
 */