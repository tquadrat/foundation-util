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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.JavaUtils.loadClass;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.JavaUtils;

/**
 *  Test for the methods
 *  {@link JavaUtils#loadClass(String)},
 *  {@link JavaUtils#loadClass(String, Class)},
 *  {@link JavaUtils#loadClass(ClassLoader, String)},
 *  and
 *  {@link JavaUtils#loadClass(ClassLoader, String, Class)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestLoadClass.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.javautils.TestLoadClass" )
public class TestLoadClass extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link JavaUtils#loadClass(String)}
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader,String)};
     *  the second one is called internally by the first one.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClass1() throws Exception
    {
        skipThreadTest();

        String candidate;

        Optional<Class<?>> actual;
        Class<?> expected;

        candidate = "NotExistingClass";
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "java.lang.Object";
        expected = Object.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "boolean";
        expected = boolean.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "byte";
        expected = byte.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "char";
        expected = char.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "double";
        expected = double.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "float";
        expected = float.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "int";
        expected = int.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "long";
        expected = long.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "short";
        expected = short.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "void";
        expected = Void.class;
        actual = loadClass( candidate );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );
    }   //  testLoadClass1()

    /**
     *  Tests for the methods
     *  {@link JavaUtils#loadClass(String, Class)}
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader, String, Class)};
     *  the second one is called internally by the first one.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClass2() throws Exception
    {
        skipThreadTest();

        String candidate;

        Optional<Class<? extends Serializable>> actual;
        final Class<?> expected;

        final var implementing = Serializable.class;

        candidate = "NotExistingClass";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        /*
         * java.lang.Object does not implement Serializable.
         */
        candidate = "java.lang.Object";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "boolean";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "byte";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "char";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "double";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "float";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "int";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "long";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "short";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "void";
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isEmpty() );

        candidate = "java.lang.String";
        expected = String.class;
        actual = loadClass( candidate, implementing );
        assertNotNull( actual );
        assertTrue( actual.isPresent() );
        assertEquals( actual.get(), expected );

        candidate = "java.lang.String";
        assertTrue( loadClass( candidate, String.class ).isPresent() );

        candidate = "java.lang.String";
        assertFalse( loadClass( candidate, UUID.class ).isPresent() );
    }   //  testLoadClass2()

    /**
     *  {@link JavaUtils#loadClass(String)},
     *  {@link JavaUtils#loadClass(String, Class)},
     *  {@link JavaUtils#loadClass(ClassLoader, String)},
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader, String, Class)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClassWithEmptyClassNameArgument() throws Exception
    {
        skipThreadTest();

        final var candidate = EMPTY_STRING;
        final var classLoader = getClass().getClassLoader();
        final Class<?> implementing = Serializable.class;

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            loadClass( candidate );
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
            loadClass( candidate, implementing );
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
            loadClass( classLoader, candidate );
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
            loadClass( classLoader, candidate, implementing );
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
    }   //  testLoadClassWithEmptyClassNameArgument()

    /**
     *  {@link JavaUtils#loadClass(ClassLoader, String)},
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader, String, Class)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClassWithNullClassLoaderArgument() throws Exception
    {
        skipThreadTest();

        final var candidate = "java.lang.String";
        final ClassLoader classLoader = null;
        final Class<?> implementing = Serializable.class;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            loadClass( classLoader, candidate );
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
            loadClass( classLoader, candidate, implementing );
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
    }   //  testLoadClass1WithNullClassLoaderArgument()

    /**
     *  {@link JavaUtils#loadClass(String)},
     *  {@link JavaUtils#loadClass(String, Class)},
     *  {@link JavaUtils#loadClass(ClassLoader, String)},
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader, String, Class)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClassWithNullClassNameArgument() throws Exception
{
    skipThreadTest();

    final String candidate = null;
    final var classLoader = getClass().getClassLoader();
    final Class<?> implementing = Serializable.class;

    final Class<? extends Throwable> expectedException = NullArgumentException.class;
    try
    {
        loadClass( candidate );
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
        loadClass( candidate, implementing );
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
        loadClass( classLoader, candidate );
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
        loadClass( classLoader, candidate, implementing );
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
}   //  testLoadClassWithNullClassNameArgument()

    /**
     *  {@link JavaUtils#loadClass(String, Class)}
     *  and
     *  {@link JavaUtils#loadClass(ClassLoader, String, Class)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testLoadClassWithNullImplementingArgument() throws Exception
    {
        skipThreadTest();

        final var candidate = "java.lang.String";
        final var classLoader = getClass().getClassLoader();
        final Class<?> implementing = null;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            loadClass( candidate, implementing );
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
            loadClass( classLoader, candidate, implementing );
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
    }   //  testLoadClassWithNullImplementingArgument()
}
//  class TestLoadClass

/*
 *  End of File
 */