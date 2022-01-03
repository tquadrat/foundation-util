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

package org.tquadrat.foundation.util.uniqueidutils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.UniqueIdUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tquadrat.foundation.util.UniqueIdUtils.UUID_Size;
import static org.tquadrat.foundation.util.UniqueIdUtils.randomUUID;

/**
 *  This class tests the constant
 *  {@link UniqueIdUtils#UUID_Size}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestUUIDSize.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestUUIDSize" )
public class TestUUIDSize extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the constant
     *  {@link UniqueIdUtils#UUID_Size}.
     */
    @Test
    public final void testUUIDSize()
    {
        skipThreadTest();

        final var candidate = randomUUID();
        assertEquals( UUID_Size, candidate.toString().length() );
    }   //  testUUIDSize()
}
//  class TestUUIDSize

/*
 *  End of File
 */