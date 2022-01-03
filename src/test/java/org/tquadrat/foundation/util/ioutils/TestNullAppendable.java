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

package org.tquadrat.foundation.util.ioutils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.IOUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.IOUtils.getNullAppendable;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.IOUtils.NullAppendable}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestNullAppendable.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestNullAppendable.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.ioutils.TestNullAppendable" )
public class TestNullAppendable extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the method
     *  {@link IOUtils#getNullAppendable}.
     *
     *  @throws Exception Something went unexpectedly wrong.
     */
    @Test
    final void testNullAppendable() throws Exception
    {
        skipThreadTest();

        final Appendable appendable = getNullAppendable();
        assertNotNull( appendable );

        try
        {
            appendable.append( '\n' );
            appendable.append( "This is a String" );
            appendable.append( "This is a String", 3, 6 );
        }
        catch( final Throwable t )
        {
            fail( "Exception '" + t.toString() + "' was thrown" );
        }
    }   //  testNullAppendable()
}
//  class TestNullAppendable

/*
 *  End of File
 */