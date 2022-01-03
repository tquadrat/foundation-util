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

package org.tquadrat.foundation.util.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.Template.findVariables;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for the method
 *  {@link org.tquadrat.foundation.util.Template#findVariables(CharSequence)}
 *  from class
 *  {@link org.tquadrat.foundation.util.Template}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestFindVariables.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestFindVariables.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.template.TestFindVariables" )
public class TestFindVariables extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link org.tquadrat.foundation.util.Template#findVariables(CharSequence)}.
     */
    @Test
    final void testFindVariables()
    {
        skipThreadTest();

        var result = findVariables( null );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        result = findVariables( EMPTY_STRING );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        result = findVariables( "No Variable" );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        result = findVariables( "${} is not valid" );
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        result = findVariables( "${_} is valid" );
        assertNotNull( result );
        assertFalse( result.isEmpty() );
        assertEquals( 1, result.size() );
        assertTrue( result.contains( "_" ) );

        result = findVariables( "${Var1} and ${Var2} are valid variables, but ${} is not" );
        assertNotNull( result );
        assertFalse( result.isEmpty() );
        assertEquals( 2, result.size() );
        assertTrue( result.containsAll( Set.of( "Var1", "Var2" ) ) );

        result = findVariables( "${Var1} and ${Var2} are valid variables, and they will appear here again: ${Var1}, followed by ${Var2}." );
        assertNotNull( result );
        assertFalse( result.isEmpty() );
        assertEquals( 2, result.size() );
        assertTrue( result.containsAll( Set.of( "Var1", "Var2" ) ) );
    }   //  testFindVariables()
}
//  class TestFindVariables

/*
 *  End of File
 */