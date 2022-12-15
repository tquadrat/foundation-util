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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.SystemUtils.getRandom;
import static org.tquadrat.foundation.util.UniqueIdUtils.newTSID;
import static org.tquadrat.foundation.util.UniqueIdUtils.tsidFromNumber;
import static org.tquadrat.foundation.util.UniqueIdUtils.tsidFromString;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.BlankArgumentException;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.TSID;
import org.tquadrat.foundation.util.stringconverter.TSIDStringConverter;

/**
 *  Tests for the creation of
 *  {@link TSID}
 *  instances.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestTSIDCreation.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TestTSIDCreation.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestTSIDCreation" )
public class TestTSIDCreation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides the numeric TSIDs for the conversion tests.
     *
     *  @return A stream with random numbers.
     */
    static final Stream<Long> randomValueProvider()
    {
        final var random = getRandom();
        final var retValue = LongStream.generate( random::nextLong )
            .limit( 1000 )
            .map( l -> l << 24 )
            .map( l -> l & random.nextLong() )
            .map( Math::abs ) // Only positive numbers are allowed.
            .boxed();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  randomValueProvider()

    /**
     *  Extended test for collisions.
     *
     *  @throws Exception    Something unexpected went wrong.
     */
    @Test
    final void testCollision() throws Exception
    {
        final var threadCount = 16;
        final var iterationCount = 100_000;
        final var hasCollisions = new AtomicBoolean( false );

        final var endLatch = new CountDownLatch( threadCount );

        final ConcurrentMap<TSID, Integer> tsidMap = new ConcurrentHashMap<>();

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
                            final var tsid = newTSID();
                            assertNull( tsidMap.put( tsid, Integer.valueOf( (threadId * iterationCount) + j ) ), "TSID collision detected" );
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
                        err.printf( "Exception in Thread '%s'", t.getName() );
                        e.printStackTrace( err );
                    }
                } );
            thread.start();
        }
        endLatch.await();
        assertFalse( hasCollisions.get() );

        out.printf(  "%d threads generated %,d TSIDs in %d ms%n", threadCount, tsidMap.size(), (nanoTime() - startNanos) / 1_000_000 );
    }   //  testCollision()

    /**
     *  Tests the conversion.
     *
     *  @throws Exception    Something unexpected went wrong.
     */
    @ParameterizedTest
    @MethodSource( "org.tquadrat.foundation.util.uniqueidutils.TestTSIDCreation#randomValueProvider" )
    final void testConversion( final Long id ) throws Exception
    {
        skipThreadTest();

        final var tsid = tsidFromNumber( id );
        assertNotNull( tsid );
        final var s = tsid.toString();
        assertEquals( s, TSIDStringConverter.INSTANCE.toString( tsid ) );
        assertEquals( tsid, tsidFromString( s ) );
        assertEquals( id, tsid.asLong() );
        assertEquals( tsid, TSIDStringConverter.INSTANCE.fromString( s ) );
    }   //  testConversion()

    /**
     *  Tests whether TSIDs will be created in an increasing sequence.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testSequence() throws Exception
    {
        skipThreadTest();

        final Collection<TSID> set = new HashSet<>();
        var previous = newTSID();
        set.add( previous );
        for( var i = 0; i < 1000; ++i )
        {
            final var current = newTSID();
            assertTrue( current.compareTo( previous ) > 0 );
            assertTrue( set.add( current ), "Collision!" );
            previous = current;
        }
    }   //  testSequence()

    /**
     *  Tests for the validation.
     *
     * @throws Exception    Something unexpected went wrong.
     */
    @Test
    final void testValidation() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> tsidFromString( null ) );
        assertThrows( EmptyArgumentException.class, () -> tsidFromString( EMPTY_STRING ) );
        assertThrows( BlankArgumentException.class, () -> tsidFromString( "  " ) );

        assertThrows( ValidationException.class, () -> tsidFromString( "1234567890ABCD" ) );
        assertThrows( ValidationException.class, () -> tsidFromString( "X1234567890" ) );

        //noinspection SpellCheckingInspection
        assertThrows( ValidationException.class, () -> tsidFromString( "XUUUUUUUUUUUUU" ) );
        assertThrows( ValidationException.class, () -> tsidFromString( "X123-456-789-0" ) );
    }   //  testValidation()
}
//  class TestTSIDCreation

/*
 *  End of File
 */