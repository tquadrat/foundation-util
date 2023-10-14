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

package org.tquadrat.foundation.util.javautils;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.JavaUtils.composeSetterName;
import static org.tquadrat.foundation.util.JavaUtils.isSetter;
import static org.tquadrat.foundation.util.JavaUtils.retrieveSetter;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;
import org.tquadrat.foundation.util.helper.Candidate;

/**
 *  Test for the methods
 *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
 *  <br>Uses the class
 *  {@link Candidate}
 *  for the tests.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestRetrieveSetter.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestRetrieveSetter" )
public class TestRetrieveSetter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the usage of the method
     *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveSetter() throws Exception
    {
        skipThreadTest();

        String propertyName;
        Optional<Method> actual;

        propertyName = "attribute1";
        actual = retrieveSetter( Candidate.class, propertyName, String.class, true );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertTrue( isSetter( actual.get() ) );
        assertEquals( composeSetterName( propertyName ), actual.get().getName() );

        propertyName = "attribute3";
        actual = retrieveSetter( Candidate.class, propertyName, String.class, true );
        assertNotNull( actual );
        assertFalse( actual.isPresent() );

        actual = retrieveSetter( Candidate.class, propertyName, String.class, false );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertFalse( isSetter( actual.get() ) ); // ... because the method is private
        assertEquals( composeSetterName( propertyName ), actual.get().getName() );

        assertNull( actual.get().invoke( new Candidate(), "String" ) );

        /*
         * Wrong type.
         */
        propertyName = "attribute1";
        actual = retrieveSetter( Candidate.class, propertyName, Date.class, true );
        assertNotNull( actual );
        assertFalse( actual.isPresent() );

        /*
         * Unknown name.
         */
        propertyName = "attributeUnknown";
        actual = retrieveSetter( Candidate.class, propertyName, String.class, true );
        assertNotNull( actual );
        assertFalse( actual.isPresent() );
    }   //  testRetrieveSetter()

    /**
     *  Tests the failure of the method
     *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveSetterWithEmptyPropertyArgument() throws Exception
    {
        skipThreadTest();

        /*
         * property may not be the empty string.
         */
        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            final var result = retrieveSetter( Candidate.class, EMPTY_STRING, String.class, true );
            assertNotNull( result );
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
    }   //  testRetrieveSetterWithEmptyPropertyArgument()

    /**
     *  Tests the failure of the method
     *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveSetterWithNullClassArgument() throws Exception
    {
        skipThreadTest();

        /*
         * beanClass may not be null.
         */
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            final var result = retrieveSetter( null, "attribute1", String.class, true );
            assertNotNull( result );
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
   }   //  testRetrieveSetterWithNullClassArgument()

    /**
     *  Tests the failure of the method
     *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveSetterWithNullPropertyArgument() throws Exception
    {
        skipThreadTest();

        /*
         * property may not be null.
         */
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            final var result = retrieveSetter( Candidate.class, null, String.class, true );
            assertNotNull( result );
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
    }   //  testRetrieveSetterWithNullPropertyArgument()

    /**
     *  Tests the failure of the method
     *  {@link JavaUtils#retrieveSetter(Class, String, Class, boolean)}.<br>
     *  <br>Uses the class
     *  {@link Candidate}
     *  for the tests.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testRetrieveSetterWithNullTypeArgument() throws Exception
    {
        skipThreadTest();

        /*
         * beanClass may not be null.
         */
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            final var result = retrieveSetter( Candidate.class, "attribute1", null, true );
            assertNotNull( result );
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
    }   //  testRetrieveSetterWithNullTypeArgument()
}
//  class TestRetrieveSetter

/*
 *  End of File
 */