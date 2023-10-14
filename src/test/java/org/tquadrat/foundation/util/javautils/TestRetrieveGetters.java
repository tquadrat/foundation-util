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

package org.tquadrat.foundation.util.javautils;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.JavaUtils.retrieveGetters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;
import org.tquadrat.foundation.util.helper.Candidate;

/**
 *  Test for the methods
 *  {@link JavaUtils#retrieveGetters(Object)}.<br>
 *  <br>Uses the class
 *  {@link Candidate}
 *  for the tests.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestRetrieveGetters.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestRetrieveGetters" )
public class TestRetrieveGetters extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for the method
     *  {@link JavaUtils#retrieveGetters(Object)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @SuppressWarnings( "MisorderedAssertEqualsArguments" )
    @Test
    final void testRetrieveGetters() throws Exception
    {
        skipThreadTest();

        /*
         * Candidate do have only three getters! If the size check fails, check
         * the definition for the class Candidate first!
         * It could have been changed ...
         */
        final var getters = retrieveGetters( new Candidate() );
        assertNotNull( getters );
        for( var getter : getters ) out.println( getter.getName() );
        assertEquals( 4, getters.length );
    }   //  testRetrieveGetters()

    /**
     *  Some tests for the method
     *  {@link JavaUtils#retrieveGetters(Object)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveGettersWithNullArgument() throws Exception
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            retrieveGetters( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testRetrieveGetters()
}
//  class TestRetrieveGetters

/*
 *  End of File
 */