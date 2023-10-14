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
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazyMap}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazyMapImpl}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazyMap.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestLazyMap.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestLazyMap" )
public class TestLazyMap extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link LazyMap#of(java.util.Map)}.
     */
    @SuppressWarnings( "cast" )
    @Test
    final void testOf()
    {
        skipThreadTest();

        final Map<String,String> map = new HashMap<>();

        final var candidate = LazyMap.of( map );
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
     *  {@link LazyMap#of(java.util.Map)}.
     */
    @Test
    final void testOfNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazyMap.of( null );
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
    }   //  testOfNull()

    /**
     *  Test for the method
     *  {@link LazyMap#use(java.util.function.Supplier)}.
     */
    @SuppressWarnings( {"cast", "unlikely-arg-type"} )
    @Test
    final void testUse()
    {
        skipThreadTest();

        String key, value;

        LazyMap<String,String> candidate = LazyMap.use( HashMap::new );
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
        candidate = LazyMap.use( HashMap::new );
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
        final Map<String,String> other = new HashMap<>();
        candidate = LazyMap.of( other );
        candidate.ifPresent( m -> assertSame( other, m ) );
        assertEquals( other.toString(), candidate.toString() );

        other.put( "key1", "value1" );
        other.put( "key2", "value2" );
        assertEquals( other.toString(), candidate.toString() );
        assertEquals( other.hashCode(), candidate.hashCode() );

        //noinspection SimplifiableAssertion
        assertFalse( candidate.equals( null ) );
        //noinspection SimplifiableAssertion,EqualsBetweenInconvertibleTypes,EqualsBetweenInconvertibleTypes
        assertFalse( candidate.equals( "String" ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( candidate ) );
        //noinspection SimplifiableAssertion
        assertTrue( candidate.equals( other ) );

        candidate.putAll( other );
    }   //  testUse()

    /**
     *  Test for the method
     *  {@link LazyMap#use(java.util.function.Supplier)}.
     */
    @Test
    final void testUseNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            LazyMap.use( null );
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
    }   //  testUseNull()
}
//  class TestLazyMap

/*
 *  End of File
 */