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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the interface
 *  {@link LazySortedMap}
 *  and its implementation
 *  {@link org.tquadrat.foundation.util.internal.LazySortedMapImpl}.<br>
 *  <br>In particular, we test with a supplier that creates not only the map,
 *  but will add contents to it as well.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLazySortedMapUse.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 10
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestLazySortedMapUse.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestLazySortedMapUse" )
public class TestLazySortedMapUse extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link LazySortedMap#use(java.util.function.Supplier)}.
     */
    @SuppressWarnings( "cast" )
    @Test
    final void testUse()
    {
        skipThreadTest();

        String key;

        LazyMap<String,String> candidate;
        candidate = LazySortedMap.use( true, () ->
        {
            final SortedMap<String,String> map = new TreeMap<>();
            map.put( "presetKey1", "presetValue1" );
            map.put( "presetKey2", "presetValue2" );
            return map;
        } );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );
        assertEquals( "{presetKey1=presetValue1, presetKey2=presetValue2}", candidate.toString() );

        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );

        candidate = LazySortedMap.use( true, () ->
        {
            final SortedMap<String,String> map = new TreeMap<>();
            map.put( "presetKey1", "presetValue1" );
            map.put( "presetKey2", "presetValue2" );
            return map;
        } );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );

        key = "presetKey1";
        assertNotNull( candidate.get( key ) );
        assertTrue( candidate.isPresent() );

        candidate = LazySortedMap.use( true, () ->
        {
            final SortedMap<String,String> map = new TreeMap<>();
            map.put( "presetKey1", "presetValue1" );
            map.put( "presetKey2", "presetValue2" );
            return map;
        } );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );
        assertEquals( "{presetKey1=presetValue1, presetKey2=presetValue2}", candidate.toString() );

        key = "key";
        assertNull( candidate.get( key ) );
        assertTrue( candidate.isPresent() );

        candidate = LazySortedMap.use( true, () ->
        {
            final SortedMap<String,String> map = new TreeMap<>();
            map.put( "presetKey1", "presetValue1" );
            map.put( "presetKey2", "presetValue2" );
            return map;
        } );
        assertNotNull( candidate );
        assertTrue( candidate instanceof Map );
        assertFalse( candidate.isPresent() );
        assertEquals( "{presetKey1=presetValue1, presetKey2=presetValue2}", candidate.toString() );

        final var value = "value";
        assertNull( candidate.put( key, value ) );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );
        assertEquals( value, candidate.get( key ) );

        candidate.clear();
        assertTrue( candidate.isEmpty() );
        assertTrue( candidate.isPresent() );
    }   //  testUse()
}
//  class TestLazySortedMapUse

/*
 *  End of File
 */