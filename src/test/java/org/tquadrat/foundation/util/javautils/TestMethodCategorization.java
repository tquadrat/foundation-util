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
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;
import org.tquadrat.foundation.util.helper.Candidate;

import javax.lang.model.element.Element;
import java.lang.reflect.Method;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.JavaUtils.isEquals;
import static org.tquadrat.foundation.util.JavaUtils.isGetter;
import static org.tquadrat.foundation.util.JavaUtils.isHashCode;
import static org.tquadrat.foundation.util.JavaUtils.isMain;
import static org.tquadrat.foundation.util.JavaUtils.isSetter;
import static org.tquadrat.foundation.util.JavaUtils.isToString;
import static org.tquadrat.foundation.util.JavaUtils.loadClass;
import static org.tquadrat.foundation.util.StringUtils.format;

/**
 *  Tests for the methods
 *  {@link JavaUtils#isEquals(Method)},
 *  {@link JavaUtils#isGetter(Method)},
 *  {@link JavaUtils#isHashCode(Method)},
 *  {@link JavaUtils#isMain(Method)},
 *  {@link JavaUtils#isSetter(Method)}, and
 *  {@link JavaUtils#isToString(Method)}.<br>
 *  <br>The test is using the class
 *  {@link Candidate}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestMethodCategorization.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestMethodCategorization" )
public class TestMethodCategorization extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link JavaUtils#isEquals(Method)},
     *  {@link JavaUtils#isGetter(Method)},
     *  {@link JavaUtils#isHashCode(Method)},
     *  {@link JavaUtils#isMain(Method)},
     *  {@link JavaUtils#isSetter(Method)}, and
     *  {@link JavaUtils#isToString(Method)}.<br>
     *  <br>The test is using the class
     *  {@link Candidate}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testMethodCategorization() throws Exception
    {
        skipThreadTest();

        final var candidateClassName = "org.tquadrat.foundation.util.helper.Candidate";
        final var candidateContainer = loadClass( candidateClassName );
        assertTrue( candidateContainer.isPresent(), () -> format( "Candidate class '%s' could not be found", candidateClassName ) );
        final var candidateClass = candidateContainer.get();
        Method candidate;

        candidate = candidateClass.getDeclaredMethod( "doNothing" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getDeclaredMethod( "doNothingStatic" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "equals", Object.class );
        assertNotNull( candidate );
        assertTrue( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "equals" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "getAttribute1" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertTrue( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "isAttribute2" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertTrue( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "get" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "getClass" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "getNothing" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "getter" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "is" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "isNot" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertTrue( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "hashCode" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertTrue( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "hashCode", int.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "main", String [].class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertTrue( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "main" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "setAttribute1", String.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertTrue( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "setAttribute2", boolean.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertTrue( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "set", boolean.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "setFlag", String.class, boolean.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );

        candidate = candidateClass.getMethod( "toString" );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertTrue( isToString( candidate ) );

        candidate = candidateClass.getMethod( "toString", Appendable.class );
        assertNotNull( candidate );
        assertFalse( isEquals( candidate ) );
        assertFalse( isGetter( candidate ) );
        assertFalse( isHashCode( candidate ) );
        assertFalse( isMain( candidate ) );
        assertFalse( isSetter( candidate ) );
        assertFalse( isToString( candidate ) );
    }   //  testMethodCategorization()

    /**
     *  Tests for the methods
     *  {@link JavaUtils#isEquals(Method)},
     *  {@link JavaUtils#isGetter(Method)},
     *  {@link JavaUtils#isHashCode(Method)},
     *  {@link JavaUtils#isMain(Method)},
     *  {@link JavaUtils#isSetter(Method)}, and
     *  {@link JavaUtils#isToString(Method)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testMethodCategorizationWithNullArgument() throws Exception
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            isEquals( null );
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

        try
        {
            isGetter( (Method) null );
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

        try
        {
            isGetter( (Element) null );
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

        try
        {
            isHashCode( null );
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

        try
        {
            isMain( null );
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

        try
        {
            isSetter( (Method) null );
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

        try
        {
            isSetter( (Element) null );
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

        try
        {
            isToString( null );
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
    }   //  testMethodCategorizationWithNullArgument()
}
//  class TestMethodCategorization

/*
 *  End of File
 */