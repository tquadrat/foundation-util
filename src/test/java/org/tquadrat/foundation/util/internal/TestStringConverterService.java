/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.internal.StringConverterService;
import org.tquadrat.foundation.testutil.TestBaseClass;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *  Some tests for the class
 *  {@link org.tquadrat.foundation.lang.internal.StringConverterService}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version TestStringConverterService: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 */
@ClassVersion( sourceVersion = "TestStringConverterService: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.internal.TestStringConverterService" )
public class TestStringConverterService extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Check whether we have at least one instance of an implementation for
     *  {@link org.tquadrat.foundation.lang.StringConverter}.
     */
    @Test
    final void testExistence()
    {
        skipThreadTest();

        final var registry = StringConverterService.loadConverters();
        assertNotNull( registry );
        assertFalse( registry.isEmpty() );

        final var keyClasses = StringConverterService.listInstances();
        assertNotNull( keyClasses );
        assertFalse( keyClasses.isEmpty() );

        keyClasses.forEach( c -> out.println( c.getName() ) );

        assertTrue( keyClasses.contains( BigDecimal.class ) );
        assertTrue( keyClasses.contains( TimeUnit.class ) );
        assertTrue( keyClasses.contains( Instant.class ) );
        assertTrue( keyClasses.contains( LocalTime.class ) );
    }   //  testExistence()

    /**
     *  Validates
     *  {@link StringConverterService}
     *  as a utility class.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( StringConverterService.class ) );
    }   //  validateClass()
}
//  class TestStringConverterService

/*
 *  End of File
 */