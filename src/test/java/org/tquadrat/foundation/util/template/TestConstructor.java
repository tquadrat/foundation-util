/*
 * ============================================================================
 *  Copyright © 2002-2026 by Thomas Thrien.
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

package org.tquadrat.foundation.util.template;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Template;

/**
 *  <p>{@summary Test the constructor for
 *  {@link Template}.}</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestConstructor.java 1239 2026-05-10 22:34:21Z tquadrat $
 *  @since 0.25.4
 */
@ClassVersion( sourceVersion = "$Id: TestConstructor.java 1239 2026-05-10 22:34:21Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.template.TestConstructor" )
public class TestConstructor extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the constructor for
     *  {@link Template}.
     *
     *  @throws Exception   Something went awfully wrong.
     */
    @Test
    final void testConstructor() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> new Template( null ) );
        var candidate = assertDoesNotThrow( () -> new Template( EMPTY_CHARSEQUENCE ) );
        assertInstanceOf( Template.class, candidate );
        candidate = assertDoesNotThrow( () -> new Template( "   " ) );
        assertInstanceOf( Template.class, candidate );
        candidate = assertDoesNotThrow( () -> new Template( """
            1234567890
            ${name}
            Something ${name} else
            """ ) );
        assertInstanceOf( Template.class, candidate );
    }   //  testConstructor()
}
//  class TestConstructor

/*
 *  End of File
 */