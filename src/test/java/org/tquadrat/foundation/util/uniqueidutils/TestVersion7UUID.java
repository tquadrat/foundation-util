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
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.UniqueIdUtils.version7UUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for version 7 UUIDs.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestVersion7UUID.java 1078 2023-10-19 14:39:47Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TestVersion7UUID.java 1078 2023-10-19 14:39:47Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestVersion7UUID" )
public class TestVersion7UUID extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the behaviour of
     *  {@link AtomicInteger#getAndIncrement()}
     *  on an overflow.
     *
     *  @throws Exception    Something unexpected went wrong.
     */
    @Test
    final void testAtomicOverflow() throws Exception
    {
        final var candidate = new AtomicInteger( Integer.MAX_VALUE - 10 );
        out.printf( "Before: %d%n", candidate.get() );
        for( var i = 0; i < 100; ++i )
        {
            try
            {
                candidate.getAndIncrement();
            }
            catch( final Exception e )
            {
                fail( "Overflow kills", e );
            }
        }
        out.printf( "After : %d%n", candidate.get() );
    }   //  testAtomicOverflow()

    /**
     *  Extended test for collisions.
     *
     *  @throws Exception    Something unexpected went wrong.
     */
    @Test
    final void testVersion7UUIDCollision() throws Exception
    {
        final var threadCount = 32;
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
                            final var uuid = version7UUID();
                            assertNull( uuidMap.put( uuid, Integer.valueOf( (threadId * iterationCount) + j ) ), "UUID collision detected" );
                        }
                    }
                    finally
                    {
                        endLatch.countDown();
                    }
                } );
            //noinspection OverlyLongLambda
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

        out.printf(  "%d threads generated %,d version 7 UUIDs in %d ms%n", threadCount, uuidMap.size(), (nanoTime() - startNanos) / 1_000_000 );
    }   //  testVersion7UUIDCollision()

    /**
     *  Tests whether timebased UUIDs will be created in an increasing sequence.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testVersion7UUIDSequence() throws Exception
    {
        skipThreadTest();

        final Collection<UUID> set = new HashSet<>();
        var previous = version7UUID();
        set.add( previous );
        for( var i = 0; i < 1_000_000; ++i )
        {
            final var current = version7UUID();
            final var message = " Current: %s - Previous: %s".formatted( current.toString(), previous.toString() );
            assertTrue( current.compareTo( previous ) > 0, "Sequence!" + message );
            assertTrue( set.add( current ), "Collision!" + message );
            previous = current;
        }
    }   //  testVersion7UUIDSequence()
}
//  class TestVersion7UUID

/*
 *  End of File
 */