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

package org.tquadrat.foundation.util.internal;

import static java.lang.System.out;
import static java.util.Arrays.sort;
import static java.util.Collections.emptyList;
import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.SystemUtils.getRandom;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.Comparators.KeyProvider;

/**
 *  Tests for the class
 *  {@link ListBasedComparator}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestListBasedComparator.java 883 2021-03-02 18:39:20Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestListBasedComparator.java 883 2021-03-02 18:39:20Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.internal.TestListBasedComparator" )
public class TestListBasedComparator extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link ListBasedComparator#compare(Object, Object)}.
     */
    @Test
    final void testCompare()
    {
        skipThreadTest();

        //---* The map that assigns the keys to the values *-------------------
        final var one = "eins";
        final var two = "zwei";
        final var three = "drei";
        final var four = "vier";
        final var five = "fünf";
        final var values = new String [] {one, two, three, four, five};
        final var map = IntStream.range( 0, values.length )
            .boxed()
            .collect( Collectors.toMap( i -> values [i], i -> Integer.valueOf( i + 1 ) ) );

        //---* The key provider *----------------------------------------------
        final KeyProvider<String,Integer> keyProvider = s -> map.computeIfAbsent( s, $ -> Integer.valueOf( getRandom().nextInt( 94 ) + 6 ) );

        //---* The fallback comparator *---------------------------------------
        final Comparator<String> fallbackComparator = naturalOrder();

        //---* The key list *--------------------------------------------------
        final var keysList = map.values().toArray( Integer []::new );
        sort( keysList );
        for( var i = 0; i < values.length; ++i )
        {
            assertEquals( i + 1, keysList [i].intValue() );
        }

        //---* Create the comparator *-----------------------------------------
        final var candidate = new ListBasedComparator<>( keyProvider, fallbackComparator, keysList );

        //---------------------------------------------------------------------
        for( final var value : values )
        {
            assertEquals( 0, candidate.compare( value, value ) );
        }

        assertEquals( 0, candidate.compare( "hundert", "hundert" ) );

        assertEquals( -1, candidate.compare( one, two ) );
        assertEquals(  1, candidate.compare( three, two ) );
        assertEquals( -1, candidate.compare( one, "acht" ) );
        assertEquals(  1, candidate.compare( "tausend", two ) );
        assertEquals( -1, candidate.compare( "hundert", "tausend" ) );
        assertEquals(  1, candidate.compare( "tausend", "million" ) );
    }   //  testCompare()

    /**
     *  Test the constructors.
     */
    @Test
    final void testConstructorsWithNullArgument()
    {
        skipThreadTest();

        ListBasedComparator<String,String> candidate;
        final Comparator<String> defaultComparator = naturalOrder();
        final KeyProvider<String,String> keyProvider = s -> s;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        try
        {
            candidate = new ListBasedComparator<>( (String []) null );
            assertNotNull( candidate );
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
            candidate = new ListBasedComparator<>( (List<String>) null );
            assertNotNull( candidate );
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
            candidate = new ListBasedComparator<>( null, defaultComparator, EMPTY_String_ARRAY );
            assertNotNull( candidate );
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
            candidate = new ListBasedComparator<>( keyProvider, defaultComparator, (String []) null );
            assertNotNull( candidate );
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
            candidate = new ListBasedComparator<>( null, defaultComparator, emptyList() );
            assertNotNull( candidate );
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
            candidate = new ListBasedComparator<>( keyProvider, defaultComparator, (List<String>) null );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorsWithNullArgument()
}
//  class TestListBasedComparator

/*
 *  End of File
 */