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

package org.tquadrat.foundation.util;

import static java.lang.String.format;
import static java.util.Arrays.fill;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazySet}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazySetImpl}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazySet.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestLazySet.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestLazySet" )
public class TestLazySet extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls methods from
     *  {@link LazySet}
     *  and indirectly from
     *  {@link org.tquadrat.foundation.util.internal.LazySetImpl}
     *  to fulfil the coverage ratio.<br>
     *  <br>A real test of these methods does not make real sense as the
     *  functionality is provided by an instance that was either created by
     *  the
     *  {@link java.util.function.Supplier}
     *  given to
     *  {@link LazySet#use(java.util.function.Supplier)}
     *  or the instance of
     *  {@link java.util.Set}
     *  given to
     *  {@link LazySet#of(java.util.Set)}.
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Test
    final void cover()
    {
        skipThreadTest();

        final Consumer<String> dump = s -> {/* Do nothing */};

        final var value1 = "value1";
        final var value2 = "value2";
        final var value3 = "value3";

        final var targetArray = new String [15];
        fill( targetArray, "VALUE" );

        LazySet<String> candidate = LazySet.use( HashSet::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertFalse( candidate.addAll( emptyList() ) );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertNotNull( candidate.toString() );
        assertNotNull( candidate.toArray() );
        assertNotNull( candidate.toArray( EMPTY_String_ARRAY ) );
        assertNotNull( candidate.toArray( targetArray ) );
        assertNull( targetArray [0] );

        assertFalse( candidate.contains( value3 ) );
        assertFalse( candidate.containsAll( List.of( value1, value2 ) ) );
        //noinspection SuspiciousMethodCalls
        assertFalse( candidate.containsAll( emptyList() ) );

        candidate.clear();
        //noinspection RedundantOperationOnEmptyContainer
        candidate.forEach( dump );
        assertFalse( candidate.iterator().hasNext() );
        //noinspection ResultOfMethodCallIgnored
        candidate.parallelStream();
        assertFalse( candidate.remove( "value4" ) );
        candidate.removeIf( v -> false );
        candidate.removeAll( List.of( "value4" ) );
        candidate.retainAll( List.of( "value4" ) );
        //noinspection ResultOfMethodCallIgnored
        candidate.stream();

        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        candidate.ifPresent( list -> fail( "Is present ..." ) );

        //---* From here onwards, the lazy list is initialized *---------------

        candidate.add( value1 );
        candidate.add( value2 );
        candidate.add( value3 );
        assertTrue( candidate.isPresent() );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        final var l = new ArrayList<>( candidate );
        assertFalse( l.isEmpty() );
        assertEquals( 3, l.size() );

        candidate.init(); // May not throw an Exception, even the instance is already initialised

        assertNotNull( candidate.toString() );
        assertNotNull( candidate.toArray() );
        assertNotNull( candidate.toArray( EMPTY_String_ARRAY ) );
        assertNotNull( candidate.toArray( targetArray ) );
        assertNotNull( targetArray [0] );
        assertNotNull( targetArray [1] );
        assertNotNull( targetArray [2] );
        assertNull( targetArray [3] );

        assertTrue( candidate.contains( value3 ) );
        assertTrue( candidate.containsAll( List.of( value1, value2 ) ) );
        assertFalse( candidate.contains( "value4" ) );
        //noinspection RedundantCollectionOperation
        assertFalse( candidate.containsAll( List.of( "value4" ) ) );
        //noinspection SuspiciousMethodCalls
        assertTrue( candidate.containsAll( emptyList() ) );

        candidate.clear();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        assertTrue( candidate.iterator().hasNext() );

        candidate.removeIf( v -> false );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeIf( v -> true );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeAll( List.of( "value4" ) );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeAll( l );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        assertFalse( candidate.remove( "value4" ) );
        assertTrue( candidate.remove( value2 ) );
        assertFalse( candidate.isEmpty() );
        assertEquals( 2, candidate.size() );
        candidate.clear();
        candidate.addAll( l );

        candidate.forEach( dump );

        //noinspection ResultOfMethodCallIgnored
        candidate.parallelStream();

        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );
        candidate.retainAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.retainAll( Set.of( "value4" ) );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        //noinspection ResultOfMethodCallIgnored
        candidate.stream();

        //---* Create a new lazy list from the l list *------------------------
        final var s = new HashSet<>( l );
        candidate = LazySet.of( s );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );
        candidate.ifPresent( set -> assertSame( s, set ) );
        candidate.init(); // May not throw an Exception, even the instance is already initialised
    }   //  cover()

    /**
     *  Test for the method
     *  {@link org.tquadrat.foundation.util.internal.LazySetImpl#equals(Object)}
     */
    @SuppressWarnings( "SimplifiableAssertion" )
    @Test
    final void testEquals()
    {
        skipThreadTest();

        final var value1 = "value1";
        final var value2 = "value2";
        final var value3 = "value3";
        final var l = Set.of( value1, value2, value3 );

        final LazySet<String> candidate = LazySet.use( HashSet::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertFalse( candidate.equals( null ) );
        assertTrue( candidate.equals( candidate ) );
        assertEquals( emptySet(), candidate );
        assertTrue( candidate.equals( emptySet() ) );
        assertFalse( candidate.equals( l ) );

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertFalse( candidate.equals( null ) );
        assertTrue( candidate.equals( candidate ) );
        assertEquals( emptySet(), candidate );
        assertTrue( candidate.equals( emptySet() ) );
        assertFalse( candidate.equals( l ) );

        assertEquals( emptySet().hashCode(), candidate.hashCode() );

        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( l.size(), candidate.size() );

        assertFalse( candidate.equals( null ) );
        assertTrue( candidate.equals( candidate ) );
        assertFalse( candidate.equals( emptySet() ) );
        assertEquals( l, candidate );
        assertTrue( candidate.equals( l ) );

        assertEquals( l.hashCode(), candidate.hashCode() );
    }   //  testEquals()

    /**
     *  Test for the method
     *  {@link LazySet#of(java.util.Set)}.
     */
    @Test
    final void testOfNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazySet.of( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testOfNull()

    /**
     *  Tests for the methods that {@code LazySet} inherits from the interface
     *  {@link Set}
     *  that needs to throw a
     *  {@link NullPointerException}
     *  when called with a {@code null} argument.
     */
    @Test
    final void testSetMethodsNull()
    {
        skipThreadTest();

        /*
         * A LazySet should mimic a standard Set as close as possible, and
         * the JDK implementations of Set do throw a NullPointerException
         * when appropriate (and no NullArgumentException).
         */
        final Class<? extends Throwable> expectedException = NullPointerException.class;

        LazySet<String> candidate = LazySet.use( HashSet::new );

        try
        {
            candidate.addAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.containsAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.forEach( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.removeAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.retainAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.toArray( (String []) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* From here the candidate is initialised *------------------------
        /*
         * Exceptions are either thrown by LazyListImpl or by LinkedList.
         */
        candidate = LazySet.of( Set.of( "value1", "value2", "value3" ) );

        try
        {
            candidate.addAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.containsAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.forEach( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.removeAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.retainAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.toArray( (String[]) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSetMethodsNull()

    /**
     *  Test for the method
     *  {@link LazySet#use(java.util.function.Supplier)}.
     */
    @Test
    final void testUseNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazySet.use( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUseNull()
}
//  class TestLazySet

/*
 *  End of File
 */