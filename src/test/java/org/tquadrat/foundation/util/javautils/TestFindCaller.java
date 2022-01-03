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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

import java.util.Optional;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.JavaUtils.findCaller;
import static org.tquadrat.foundation.util.StringUtils.format;

/**
 *  Test for the method
 *  {@link JavaUtils#findCaller(int)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestFindCaller.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestFindCaller" )
public class TestFindCaller extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for the usage of
     *  {@link JavaUtils#findCaller(int)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testFindCaller() throws Exception
    {
        skipThreadTest();

        Optional<StackTraceElement> actual;

        /*
         *  Although not impossible, it is very unlikely that the call stack is
         *  that deep...
         */
        actual = findCaller( Integer.MAX_VALUE );
        assertNotNull( actual );
        assertFalse( actual.isPresent() );

        /*
         *  For the offset 0, the caller must be the utility method itself.
         */
        actual = findCaller( 0 );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get().getMethodName(), "findCaller" );

        /*
         *  For the offset 1, the caller must be this method.
         */
        actual = findCaller( 1 );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get().getMethodName(), "testFindCaller" );
    }   //  testFindCaller()

    /**
     *  Test method for the failure of
     *  {@link JavaUtils#findCaller(int)}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testFindCaller_Failure() throws Exception
    {
        skipThreadTest();

        /*
         *  An offset value less than zero is not valid.
         */
        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            findCaller( -1 );
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
    }   //  testFindCaller_Failure()
}
//  class TestFindCaller

/*
 *  End of File
 */