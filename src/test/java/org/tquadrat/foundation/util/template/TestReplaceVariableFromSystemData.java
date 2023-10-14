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

import static java.lang.String.format;
import static java.lang.System.getProperties;
import static java.lang.System.getenv;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.tquadrat.foundation.util.Template.VARIABLE_TEMPLATE;
import static org.tquadrat.foundation.util.Template.isValidVariableName;
import static org.tquadrat.foundation.util.Template.replaceVariableFromSystemData;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Objects;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for the method
 *  {@link org.tquadrat.foundation.util.Template#replaceVariableFromSystemData(CharSequence, Map...)}.
 *  from the class
 *  {@link org.tquadrat.foundation.util.Template}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestReplaceVariableFromSystemData.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestReplaceVariableFromSystemData.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.template.TestReplaceVariableFromSystemData" )
public class TestReplaceVariableFromSystemData extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates a
     *  {@link Stream}
     *  with all the names for the system properties.
     *
     *  @return The {@code Stream}.
     */
    static final Stream<String> retrieveSystemPropertyNames()
    {
        //---* The system properties *-----------------------------------------
        final var propertyKeys = getProperties().stringPropertyNames();
        final var retValue = propertyKeys.stream();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveSystemPropertyNames()

    /**
     *  Creates a
     *  {@link Stream}
     *  with all the names for the system environment entries.
     *
     *  @return The {@code Stream}.
     *
     *  @see #testReplaceSystemVariableEnvironment(String)
     */
    static final Stream<String> retrieveSystemSettingNames()
    {
        //---* The system environment *----------------------------------------
        final var envKeys = getenv().keySet();
        final var retValue = envKeys.stream();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveSystemSettingNames()

    /**
     *  Test for
     *  {@link org.tquadrat.foundation.util.Template#replaceVariableFromSystemData(CharSequence, Map...)}.
     */
    @Test
    final void testReplaceVariableFromSystemDataWithNullArgument()
    {
        skipThreadTest();

        assertNull( replaceVariableFromSystemData( null ) );
    }   //  testReplaceVariableFromSystemDataWithNullArgument()

    /**
     *  Test for
     *  {@link org.tquadrat.foundation.util.Template#replaceVariableFromSystemData(CharSequence, Map...)}.
     *
     *  @param  candidate   The name for the variable to retrieve.
     */
    @ParameterizedTest
    @MethodSource( "retrieveSystemSettingNames" )
    final void testReplaceSystemVariableEnvironment( final String candidate )
    {
        skipThreadTest();

        assumeTrue( isValidVariableName( candidate ) );

        final var text = format( VARIABLE_TEMPLATE, candidate );
        final var expected = getenv().get( candidate );
        assertEquals( expected, replaceVariableFromSystemData( text ) );
    }   //  testReplaceSystemVariablesEnvironment()

    /**
     *  Test for
     *  {@link org.tquadrat.foundation.util.Template#replaceVariableFromSystemData(CharSequence, Map...)}.
     *
     *  @param  candidate   The name for the variable to retrieve.
     */
    @ParameterizedTest
    @MethodSource( "retrieveSystemPropertyNames" )
    final void testReplaceSystemVariableProperties( final String candidate )
    {
        skipThreadTest();

        assumeTrue( isValidVariableName( candidate ) );

        final var text = format( VARIABLE_TEMPLATE, candidate );
        final var expected = Objects.toString( getProperties().getProperty( candidate ) );
        assertEquals( expected, replaceVariableFromSystemData( text ) );
    }   //  testReplaceSystemVariablesProperties()
}
//  class TestReplaceVariableFromSystemData

/*
 *  End of File
 */