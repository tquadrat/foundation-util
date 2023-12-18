/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.DateTimeUtils.replaceByCachedZoneId;
import static org.tquadrat.foundation.util.DateTimeUtils.retrieveCachedZoneId;

import java.time.ZoneId;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for
 *  {@link DateTimeUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@DisplayName( "org.tquadrat.foundation.util.TestDateTimeUtils" )
public class TestDateTimeUtils extends TestBaseClass
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The reference to the cache.
     */
    private static final Map<String,ZoneId> m_Cache;

    static
    {
        try
        {
            final var field = DateTimeUtils.class.getDeclaredField( "m_ZoneIdCache" );
            field.setAccessible( true );
            //noinspection unchecked
            m_Cache = (Map<String,ZoneId>) field.get( null );
        }
        catch( final
        NoSuchFieldException | IllegalAccessException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests for the retrieval functions.
     *
     *  @throws Exception   Something unexpected got wrong.
     */
    @Test
    final void testRetrievalMethods() throws Exception
    {
       skipThreadTest();

        ZoneId zoneId1;
        ZoneId zoneId2;

        assertTrue( m_Cache.isEmpty() );

        final var id = "Europe/Berlin";
        zoneId1 = ZoneId.of( id );
        zoneId2 = ZoneId.of( id );
        assertNotSame( zoneId1, zoneId2 );

        zoneId1 = retrieveCachedZoneId( id );
        assertNotNull( zoneId1 );
        assertFalse( m_Cache.isEmpty() );
        assertNotSame( zoneId1, zoneId2 );
        assertSame( zoneId1, retrieveCachedZoneId( id ) );
        zoneId2 = replaceByCachedZoneId( zoneId2 );
        assertSame( zoneId1, zoneId2 );
        zoneId2 = retrieveCachedZoneId( id );
        assertSame( zoneId1, zoneId2 );
    }   //  testRetrievalMethods()

    /**
     *  Validates whether the class is static.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( DateTimeUtils.class ) );
    }   //  validateClass()
}
//  class TestDateTimeUtils

/*
 *  End of File
 */