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

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.Comparators.caseInsensitiveComparator;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazySortedMap}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazySortedMapImpl}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazySortedMap.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestLazySortedMap.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestLazySortedMap" )
public class TestLazySortedMap extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link LazySortedMap#firstKey()}
     *  and
     *  {@link LazySortedMap#lastKey()}
     *  on an empty map.
     */
    @Test
    final void testFirstLast()
    {
        skipThreadTest();

        final LazySortedMap<String,String> candidate = LazySortedMap.use( TreeMap::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        final Class<? extends Throwable> expectedException = NoSuchElementException.class;
        try
        {
            candidate.firstKey();
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
            candidate.lastKey();
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        try
        {
            candidate.firstKey();
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
            candidate.lastKey();
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testFirstLast()

    /**
     *  Test for the method
     *  {@link LazySortedMap#of(java.util.SortedMap)}.
     */
    @SuppressWarnings( "cast" )
    @Test
    final void testOf()
    {
        skipThreadTest();

        final SortedMap<String,String> map = new TreeMap<>();

        final var candidate = LazySortedMap.of( map );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        final var key = "key";
        //noinspection RedundantOperationOnEmptyContainer
        assertNull( candidate.get( key ) );

        final var value = "value";
        assertNull( candidate.put( key, value ) );
        assertFalse( candidate.isEmpty() );
        assertEquals( value, candidate.get( key ) );

        candidate.clear();
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        candidate.ifPresent( m -> assertSame( map, m ) );
    }   //  testOf()

    /**
     *  Test for the method
     *  {@link LazySortedMap#of(java.util.SortedMap)}.
     */
    @Test
    final void testOfNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazySortedMap.of( null );
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
     *  Tests for the methods that came new for
     *  {@link LazySortedMap}.
     */
    @Test
    final void testSortedMap()
    {
        skipThreadTest();

        final var key1 = "key1";
        final var key2 = "key2";
        final var key3 = "key3";
        final var key4 = "key4";
        final var key5 = "key5";
        final var key6 = "key6";
        final var key7 = "key7";
        final var key8 = "key8";

        final var value1 = "value1";
        final var value2 = "value2";
        final var value3 = "value3";
        final var value4 = "value4";
        final var value5 = "value5";
        final var value6 = "value6";
        final var value7 = "value7";
        final var value8 = "value8";

        final SortedMap<String,String> map = new TreeMap<>();
        assertTrue( map.isEmpty() );

        LazySortedMap<String,String> candidate = LazySortedMap.use( TreeMap::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        assertEquals( map.headMap( key3 ), candidate.headMap( key3 ) );
        assertEquals( map.tailMap( key6 ), candidate.tailMap( key6 ) );

        assertEquals( map.subMap( key4, key5 ), candidate.subMap( key4, key5 ) );

        candidate.init();
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        assertEquals( map.headMap( key3 ), candidate.headMap( key3 ) );
        assertEquals( map.tailMap( key6 ), candidate.tailMap( key6 ) );

        assertEquals( map.subMap( key4, key5 ), candidate.subMap( key4, key5 ) );

        map.put( key1, value1 );
        map.put( key2, value2 );
        map.put( key3, value3 );
        map.put( key4, value4 );
        map.put( key5, value5 );
        map.put( key6, value6 );
        map.put( key7, value7 );
        map.put( key8, value8 );

        candidate.put( key1, value1 );
        candidate.put( key2, value2 );
        candidate.put( key3, value3 );
        candidate.put( key4, value4 );
        candidate.put( key5, value5 );
        candidate.put( key6, value6 );
        candidate.put( key7, value7 );
        candidate.put( key8, value8 );

        assertFalse( map.isEmpty() );
        assertTrue( candidate.isPresent() );
        assertFalse( candidate.isEmpty() );

        assertEquals( map.firstKey(), candidate.firstKey() );
        assertEquals( map.lastKey(), candidate.lastKey() );

        assertEquals( map.headMap( key3 ), candidate.headMap( key3 ) );
        assertEquals( map.tailMap( key6 ), candidate.tailMap( key6 ) );

        assertEquals( map.subMap( key4, key5 ), candidate.subMap( key4, key5 ) );

        //---* Another instance *----------------------------------------------
        candidate = LazySortedMap.use( TreeMap::new );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        assertNull( candidate.comparator() ); // null indicating natural order
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        candidate = LazySortedMap.use( () -> new TreeMap<>( caseInsensitiveComparator() ) );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );

        assertNotNull( candidate.comparator() );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
    }   //  testSortedMap()

    /**
     *  Test for the method
     *  {@link LazySortedMap#use(java.util.function.Supplier)}.
     */
    @SuppressWarnings( {"cast", "unlikely-arg-type"} )
    @Test
    final void testUse()
    {
        skipThreadTest();

        String key, value;

        LazySortedMap<String,String> candidate = LazySortedMap.use( TreeMap::new );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.isPresent() );

        assertEquals( "[Not initialized]", candidate.toString() );

        key = "key";
        assertNull( candidate.get( key ) );
        assertFalse( candidate.isPresent() );

        value = "value";
        assertNull( candidate.put( key, value ) );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );
        assertEquals( value, candidate.get( key ) );

        candidate.clear();
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        //---* Another lazy map *----------------------------------------------
        candidate = LazySortedMap.use( TreeMap::new );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.isPresent() );

        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( emptyMap() ) );
        assertTrue( candidate.keySet().isEmpty() );
        assertTrue( candidate.values().isEmpty() );
        assertTrue( candidate.entrySet().isEmpty() );

        key = "key";
        value = "value";
        //noinspection RedundantOperationOnEmptyContainer
        assertNull( candidate.get( key ) );
        assertFalse( candidate.containsKey( key ) );
        assertFalse( candidate.containsValue( value ) );
        assertEquals( 0, candidate.size() );

        candidate.init();
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        assertNull( candidate.get( key ) );
        assertFalse( candidate.containsKey( key ) );
        assertFalse( candidate.containsValue( value ) );
        assertEquals( 0, candidate.size() );

        assertTrue( candidate.keySet().isEmpty() );
        assertTrue( candidate.values().isEmpty() );
        assertTrue( candidate.entrySet().isEmpty() );

        assertNull( candidate.put( key, value ) );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );
        assertTrue( candidate.containsKey( key ) );
        assertTrue( candidate.containsValue( value ) );
        assertEquals( 1, candidate.size() );
        assertEquals( value, candidate.get( key ) );

        assertFalse( candidate.keySet().isEmpty() );
        assertFalse( candidate.values().isEmpty() );
        assertFalse( candidate.entrySet().isEmpty() );

        assertEquals( value, candidate.remove( key ) );
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        assertNull( candidate.put( key, value ) );
        candidate.clear();
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        //---* A not so lazy map *---------------------------------------------
        final SortedMap<String,String> other = new TreeMap<>();
        candidate = LazySortedMap.of( other );
        candidate.ifPresent( m -> assertSame( other, m ) );
        assertEquals( other.toString(), candidate.toString() );

        other.put( "key1", "value1" );
        other.put( "key2", "value2" );
        assertEquals( other.toString(), candidate.toString() );
        assertEquals( other.hashCode(), candidate.hashCode() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( null ) );
        //noinspection SimplifiableAssertion,EqualsBetweenInconvertibleTypes
        assertFalse( candidate.equals( "String" ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( candidate ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( other ) );

        candidate.putAll( other );
    }   //  testUse()

    /**
     *  Test for the method
     *  {@link LazySortedMap#use(java.util.function.Supplier)}.
     */
    @Test
    final void testUseNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazySortedMap.use( null );
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
//  class TestLazySortedMap

/*
 *  End of File
 */