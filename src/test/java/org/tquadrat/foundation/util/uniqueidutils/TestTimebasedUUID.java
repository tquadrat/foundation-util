/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
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

package org.tquadrat.foundation.util.uniqueidutils;

import static java.lang.System.err;
import static java.lang.System.nanoTime;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.UniqueIdUtils.timebasedUUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for timebased UUIDs.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestTimebasedUUID.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TestTimebasedUUID.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestTimebasedUUID" )
public class TestTimebasedUUID extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Extended test for collisions.
     *
     *  @throws Exception    Something unexpected went wrong.
     */
    @Test
    final void testTimebaseUUIDCollision() throws Exception
    {
        final var threadCount = 16;
        final var iterationCount = 100_000;
        final var hasCollisions = new AtomicBoolean( false );

        final var endLatch = new CountDownLatch( threadCount );

        final ConcurrentMap<UUID,Integer> uuidMap = new ConcurrentHashMap<>();

        final var startNanos = nanoTime();

        for( var i = 0; i < threadCount; ++i )
        {
            final var threadId = i;
            //noinspection OverlyLongLambda
            final var thread = new Thread(
                () ->
                {
                    try
                    {
                        for( var j = 0; j < iterationCount; ++j )
                        {
                            final var uuid = timebasedUUID();
                            assertNull( uuidMap.put( uuid, Integer.valueOf( (threadId * iterationCount) + j ) ), "UUID collision detected" );
                        }
                    }
                    finally
                    {
                        endLatch.countDown();
                    }
                } );
            thread.setUncaughtExceptionHandler(
                (t,e) ->
                {
                    hasCollisions.set( true );
                    synchronized( err )
                    {
                        err.printf( "Exception in Thread '%s': ", t.getName() );
                        e.printStackTrace( err );
                    }
                } );
            thread.start();
        }
        endLatch.await();
        assertFalse( hasCollisions.get() );

        out.printf(  "%d threads generated %,d timebased UUIDs in %d ms%n", threadCount, uuidMap.size(), (nanoTime() - startNanos) / 1_000_000 );
    }   //  testTimebaseUUIDCollision()

    /**
     *  Tests whether timebased UUIDs will be created in an increasing sequence.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testTimebaseUUIDSequence() throws Exception
    {
        skipThreadTest();

        final Collection<UUID> set = new HashSet<>();
        var previous = timebasedUUID();
        set.add( previous );
        for( var i = 0; i < 1000; ++i )
        {
            final var current = timebasedUUID();
            assertTrue( current.compareTo( previous ) > 0 );
            assertTrue( set.add( current ), "Collision!" );
            previous = current;
        }
    }   //  testTimebaseUUIDSequence()
}
//  class TestTimebasedUUID

/*
 *  End of File
 */