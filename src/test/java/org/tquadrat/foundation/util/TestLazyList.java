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
import static java.lang.System.out;
import static java.util.Arrays.fill;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.lang.CommonConstants.NULL_STRING;
import static org.tquadrat.foundation.util.Comparators.caseInsensitiveComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazyList}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazyListImpl}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazyList.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( {"MisorderedAssertEqualsArguments", "OverlyComplexClass"} )
@ClassVersion( sourceVersion = "$Id: TestLazyList.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.LazyList and ~.internal.LazyListImpl" )
public class TestLazyList extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls methods from
     *  {@link LazyList}
     *  and indirectly from
     *  {@link org.tquadrat.foundation.util.internal.LazyListImpl}
     *  to fulfil the coverage ratio.<br>
     *  <br>A real test of these methods does not make real sense as the
     *  functionality is provided by an instance that was either created by
     *  the
     *  {@link java.util.function.Supplier}
     *  given to
     *  {@link LazyList#use(java.util.function.Supplier)}
     *  or the instance of
     *  {@link java.util.List}
     *  given to
     *  {@link LazyList#of(java.util.List)}.
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

        LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertFalse( candidate.addAll( emptyList() ) );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        assertFalse( candidate.addAll( 0, emptyList() ) );
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
        assertEquals( -1, candidate.indexOf( "value4" ) );
        assertFalse( candidate.iterator().hasNext() );
        assertEquals( -1, candidate.lastIndexOf( "value4" ) );
        assertFalse( candidate.listIterator().hasNext() );
        assertFalse( candidate.listIterator( 0 ).hasNext() );
        //noinspection ResultOfMethodCallIgnored
        candidate.parallelStream();
        assertFalse( candidate.remove( "value4" ) );
        candidate.removeIf( v -> false );
        candidate.removeAll( List.of( "value4" ) );
        candidate.replaceAll( v -> v + v );
        candidate.retainAll( List.of( "value4" ) );
        candidate.sort( caseInsensitiveComparator() );
        //noinspection ResultOfMethodCallIgnored
        candidate.stream();
        assertTrue( candidate.subList( 0, 0 ).isEmpty() );

        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        candidate.ifPresent( list -> fail( "Is present ..." ) );

        //---* From here onwards, the lazy list is initialized *---------------

        candidate.add( 0, value1 );
        candidate.add( 1, value2 );
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
        candidate.addAll( 0, l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        assertEquals( -1, candidate.indexOf( "value4" ) );
        assertEquals( -1, candidate.lastIndexOf( "value4" ) );
        assertNotEquals( -1, candidate.indexOf( "value3" ) );
        assertNotEquals( -1, candidate.lastIndexOf( "value3" ) );

        assertTrue( candidate.iterator().hasNext() );

        assertTrue( candidate.listIterator().hasNext() );
        assertTrue( candidate.listIterator( 1 ).hasNext() );

        candidate.removeIf( v -> false );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeIf( v -> true );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( 0, l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeAll( List.of( "value4" ) );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.removeAll( l );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );
        candidate.addAll( 0, l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        assertNotNull( candidate.remove( 0 ) );
        assertFalse( candidate.remove( "value4" ) );
        assertTrue( candidate.remove( value2 ) );
        assertFalse( candidate.isEmpty() );
        assertEquals( 1, candidate.size() );
        candidate.clear();
        candidate.addAll( 0, l );

        candidate.replaceAll( v -> v + v );
        candidate.sort( caseInsensitiveComparator() );

        //noinspection ResultOfMethodCallIgnored
        candidate.get( 0 );
        candidate.forEach( dump );

        //noinspection ResultOfMethodCallIgnored
        candidate.parallelStream();

        candidate.addAll( 0, l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 6, candidate.size() );
        candidate.retainAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );

        candidate.set( 1, "value4" );

        //noinspection ResultOfMethodCallIgnored
        candidate.stream();

        assertEquals( 2, candidate.subList( 0, 2 ).size() );

        //---* Create a new lazy list from the l list *------------------------
        candidate = LazyList.of( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( 3, candidate.size() );
        candidate.ifPresent( list -> assertSame( l, list ) );
        candidate.init(); // May not throw an Exception, even the instance is already initialised
    }   //  cover()

    /**
     *  Tests for the method
     *  {@link LazyList#add(int,Object)}.
     */
    @Test
    final void testAddInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            candidate.add( 1, NULL_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            candidate.add( 1, NULL_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddInvalid()

    /**
     *  Test for the method
     *  {@link org.tquadrat.foundation.util.internal.LazyListImpl#equals(Object)}
     */
    @Test
    final void testEquals()
    {
        skipThreadTest();

        final var value1 = "value1";
        final var value2 = "value2";
        final var value3 = "value3";
        final var l = List.of( value1, value2, value3 );

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( null ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( candidate ) );
        assertEquals( emptyList(), candidate );
        //noinspection SimplifiableAssertion
        assertTrue( emptyList().equals( candidate ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( emptyList() ) );
        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( l ) );
        //noinspection SimplifiableAssertion
        assertFalse( l.equals( candidate ) );

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertEquals( 0, candidate.size() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( null ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( candidate ) );
        assertEquals( emptyList(), candidate );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( emptyList() ) );
        //noinspection SimplifiableAssertion
        assertTrue( emptyList().equals( candidate ) );
        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( l ) );
        //noinspection SimplifiableAssertion
        assertFalse( l.equals( candidate ) );

        assertEquals( emptyList().hashCode(), candidate.hashCode() );

        candidate.addAll( l );
        assertFalse( candidate.isEmpty() );
        assertEquals( l.size(), candidate.size() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( null ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( candidate ) );
        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( emptyList() ) );
        //noinspection SimplifiableAssertion
        assertFalse( emptyList().equals( candidate ) );
        assertEquals( l, candidate );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( l ) );
        //noinspection SimplifiableAssertion
        assertTrue( l.equals( candidate ) );

        assertEquals( l.hashCode(), candidate.hashCode() );
    }   //  testEquals()

    /**
     *  Tests for the method
     *  {@link LazyList#get(int)}.
     */
    @Test
    final void testGetInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            //noinspection ResultOfMethodCallIgnored
            candidate.get( 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            //noinspection ResultOfMethodCallIgnored
            candidate.get( 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testGetInvalid()

    /**
     *  Tests for the method
     *  {@link LazyList#listIterator(int)}.
     */
    @Test
    final void testListIteratorInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            candidate.listIterator( 1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            candidate.listIterator( 1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testListIteratorInvalid()

    /**
     *  Tests for the methods that {@code LazyList} inherits from the interface
     *  {@link List}
     *  that needs to throw a
     *  {@link NullPointerException}
     *  when called with a {@code null} argument.
     */
    @Test
    final void testListMethodsNull()
    {
        skipThreadTest();

        /*
         * A LazyList should mimic a standard List as close as possible, and
         * the JDK implementations of List do throw a NullPointerException
         * when appropriate (and no NullArgumentException).
         */
        final Class<? extends Throwable> expectedException = NullPointerException.class;

        LazyList<String> candidate = LazyList.use( ArrayList::new );

        try
        {
            candidate.addAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.addAll( 0, null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* From here the candidate is initialised *------------------------
        /*
         * Exceptions are either thrown by LazyListImpl or by LinkedList.
         */
        candidate = LazyList.of( List.of( "value1", "value2", "value3" ) );

        try
        {
            candidate.addAll( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.addAll( 0, null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
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
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testListMethodsNull()

    /**
     *  Test for the method
     *  {@link LazyList#of(java.util.List)}.
     */
    @Test
    final void testOfNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazyList.of( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testOfNull()

    /**
     *  Tests for the method
     *  {@link LazyList#remove(int)}.
     */
    @Test
    final void testRemoveInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            candidate.remove( 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            //noinspection RedundantOperationOnEmptyContainer
            candidate.remove( 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testRemoveInvalid()

    /**
     *  Tests for the method
     *  {@link LazyList#set(int,Object)}.
     */
    @Test
    final void testSetInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            candidate.set( 1, NULL_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            candidate.set( 1, NULL_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSetInvalid()

    /**
     *  Tests for the method
     *  {@link LazyList#subList(int, int)}.
     */
    @Test
    final void testSublistInvalid()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;

        final LazyList<String> candidate = LazyList.use( ArrayList::new );
        assertFalse( candidate.isPresent() );

        try
        {
            candidate.subList( 0, 1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            candidate.subList( 0, 1 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testSublistInvalid()

    /**
     *  Test for the method
     *  {@link LazyList#use(java.util.function.Supplier)}.
     */
    @Test
    final void testUseNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazyList.use( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testUseNull()
}
//  class TestLazyList

/*
 *  End of File
 */