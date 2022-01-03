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

package org.tquadrat.foundation.util.stringutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.pad;
import static org.tquadrat.foundation.util.StringUtils.padCenter;
import static org.tquadrat.foundation.util.StringUtils.padLeft;
import static org.tquadrat.foundation.util.StringUtils.padRight;
import static org.tquadrat.foundation.util.StringUtils.repeat;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_ABBREVIATE;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_ABBREVIATE_MIDDLE;
import static org.tquadrat.foundation.util.StringUtils.Clipping.CLIPPING_NONE;
import static org.tquadrat.foundation.util.StringUtils.Padding.PADDING_CENTER;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.UnsupportedEnumError;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.util.StringUtils.Clipping;
import org.tquadrat.foundation.util.StringUtils.Padding;

/**
 *  Some tests for the methods
 *  {@link StringUtils#pad(CharSequence, int, char, Padding, boolean)},
 *  {@link StringUtils#pad(CharSequence, int, char, Padding, Clipping)},
 *  {@link StringUtils#padCenter(CharSequence, int)},
 *  {@link StringUtils#padLeft(CharSequence, int)}
 *  and
 *  {@link StringUtils#padRight(CharSequence, int)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestPad.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestPad.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestPad" )
public class TestPad extends TestBaseClass
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The test data for
     *  {@link #testPad(DataPad)}.
     *
     *  @param  expected    The expected result for the test; if
     *      {@code null} the test should fail.
     *  @param  counter The test number.
     *  @param  input   The input to process.
     *  @param  length  The target length.
     *  @param  padMode The padding mode.
     *  @param  clipMode    The clipping mode.
     */
    @SuppressWarnings( {"hiding", "javadoc", "preview"} )
    private static record DataPad( String expected, int counter, CharSequence input, int length, Padding padMode, Clipping clipMode ) { /* Empty */}

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls the protected method {@code clip()} on the clipping instance.
     *
     *  @param  clipping    The clipping instance.
     *  @param  s   The input String.
     *  @param  length  The target length.
     *  @return The result String.
     *  @throws NoSuchMethodException   The method {@code clip()} is missing.
     *  @throws InvocationTargetException   Calling {@code clip()} failed.
     *  @throws IllegalAccessException  Not allowed to invoke {@code clip()}.
     */
    private static final String callClip( final Clipping clipping, final CharSequence s, final int length ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        @SuppressWarnings( "RedundantExplicitVariableType" )
        final Class<? extends Clipping> instanceClass = clipping.getClass();
        final var callClipMethod = instanceClass.getDeclaredMethod( "clip", CharSequence.class, int.class );
        callClipMethod.setAccessible( true );
        final var retValue = callClipMethod.invoke( clipping, s, Integer.valueOf( length ) ).toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  callClip()

    /**
     *  Composes a bunch of instances of
     *  {@link DataPad}
     *  and adds them to the given
     *  {@link Builder}.
     *
     *  @param  counter The current counter.
     *  @param  builder The destination for the new instance.
     *  @param  input   The input data.
     *  @param  length  The target length.
     *  @return The current counter.
     *  @throws Exception   Something unexpected went wrong.
     */
    private static final int composeDataPad( final int counter, final Builder<? super DataPad> builder, final String input, final int length ) throws Exception
    {
        var retValue = counter;
        final var currentLength = input.length();
        String expected = null;
        DataPad dataPad = null;
        for( final var padMode : Padding.values() )
        {
            for( final var clipMode : Clipping.values() )
            {
                if( currentLength == length )
                {
                    expected = input;
                }
                else if( currentLength > length )
                {
                    switch( clipMode )
                    {
                        case CLIPPING_ABBREVIATE_MIDDLE:
                            if( length < 5 ) break;
                            //$FALL-THROUGH$
                        case CLIPPING_ABBREVIATE:
                            if( length < 4 ) break;
                            //noinspection fallthrough
                            //$FALL-THROUGH$
                            //$CASES-OMITTED$
                        default:
                            expected = callClip( clipMode, input, length );
                    }
                }
                else
                {
                    final var pad = repeat( ' ', length );
                    expected = switch( padMode )
                    {
                        case PADDING_CENTER -> {
                            final var diff = (length - currentLength);
                            final var prefix = (diff / 2) + (diff % 2);
                            yield format( "%s%s%s", pad.substring( 0, prefix ), input, pad ).substring( 0, length );
                        }
                        case PADDING_LEFT -> format( "%s%s", pad, input ).substring( currentLength );
                        case PADDING_RIGHT -> format( "%s%s", input, pad ).substring( 0, length );
                        default -> throw new UnsupportedEnumError( padMode );
                    };
                }
                dataPad = new DataPad( expected, ++retValue, input, length, padMode, clipMode );
                builder.add( dataPad );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeDataPad()

    /**
     *  Provides the test data for
     *  {@link #testPad(DataPad)}.
     *
     *  @return The test data.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<DataPad> providePadData() throws Exception
    {
        var counter = 0;
        final Builder<DataPad> builder = Stream.builder();

        counter = composeDataPad( counter, builder, "12345678", 6 );
        counter = composeDataPad( counter, builder, "12345678", 8 );
        counter = composeDataPad( counter, builder, "12345678", 12 );

        counter = composeDataPad( counter, builder, " 123456 ", 6 );
        counter = composeDataPad( counter, builder, " 123456 ", 8 );
        counter = composeDataPad( counter, builder, " 123456 ", 12 );

        counter = composeDataPad( counter, builder, "12345678", 5 );
        counter = composeDataPad( counter, builder, "12345678", 9 );
        counter = composeDataPad( counter, builder, "12345678", 13 );

        counter = composeDataPad( counter, builder, "123456789", 6 );
        counter = composeDataPad( counter, builder, "123456789", 8 );
        counter = composeDataPad( counter, builder, "123456789", 12 );

        counter = composeDataPad( counter, builder, "123456789", 5 );
        counter = composeDataPad( counter, builder, "123456789", 9 );
        composeDataPad( counter, builder, "123456789", 13 );

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  providePadData()

    /**
     *  Tests for
     *  {@link StringUtils#pad(CharSequence, int, char, Padding, boolean)},
     *  {@link StringUtils#pad(CharSequence, int, char, Padding, Clipping)},
     *  {@link StringUtils#padCenter(CharSequence, int)},
     *  {@link StringUtils#padLeft(CharSequence, int)},
     *  and
     *  {@link StringUtils#padRight(CharSequence, int)}.
     *
     *  @param  data    The data for the test.
     *  @throws Exception   Something unexpected went wrong.
     */
    @SuppressWarnings( "NestedSwitchStatement" )
    @ParameterizedTest
    @MethodSource( "providePadData" )
    final void testPad( final DataPad data ) throws Exception
    {
        skipThreadTest();

        final var expected = data.expected();
        final var input = data.input();
        final var padMode = data.padMode();
        final var clipMode = data.clipMode();
        final var length = data.length();

        //out.printf( "%s: Expected: %s - Input: %s - Len: %d - Clip: %s - Pad: %s\n", description, expected, input, length, clipMode.name(), padMode.name() );

        switch( clipMode )
        {
            case CLIPPING_ABBREVIATE ->
            {
                switch( padMode )
                {
                    case PADDING_CENTER -> assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                    case PADDING_LEFT -> {
                        assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                        assertEquals( expected, padLeft( input, length ), format( "The input: %s", input ) );
                    }
                    case PADDING_RIGHT -> {
                        assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                        assertEquals( expected, padRight( input, length ), format( "The input: %s", input ) );
                    }
                    default -> throw new UnsupportedEnumError( padMode );
                }
            }
            case CLIPPING_ABBREVIATE_MIDDLE ->
            {
                switch( padMode )
                {
                    case PADDING_CENTER ->
                    {
                        assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                        assertEquals( expected, padCenter( input, length ), format( "The input: %s", input ) );
                    }
                    case PADDING_LEFT, PADDING_RIGHT -> assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                    default -> throw new UnsupportedEnumError( padMode );
                }
            }
            case CLIPPING_CUT ->
            {
                assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                assertEquals( expected, pad( input, length, ' ', padMode, true ), format( "The input: %s", input ) );
            }
            case CLIPPING_NONE ->
            {
                assertEquals( expected, pad( input, length, ' ', padMode, clipMode ), format( "The input: %s", input ) );
                assertEquals( expected, pad( input, length, ' ', padMode, false ), format( "The input: %s", input ) );
            }
            default -> throw new UnsupportedEnumError( clipMode );
        }
    }   //  testPad()

    /**
     *  Tests for
     *  {@link StringUtils#pad(CharSequence, int, char, Padding, boolean)},
     *  {@link StringUtils#pad(CharSequence, int, char, Padding, Clipping)},
     *  {@link StringUtils#padCenter(CharSequence, int)},
     *  {@link StringUtils#padLeft(CharSequence, int)},
     *  and
     *  {@link StringUtils#padRight(CharSequence, int)}.
     */
    @Test
    final void testPadFailWithNullSArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            pad( null, 6, ' ', PADDING_CENTER, CLIPPING_NONE );
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
            pad( "String", 6, ' ', null, CLIPPING_NONE );
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
            pad( "String", 6, ' ', PADDING_CENTER, null );
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
            padCenter( null, 6 );
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
            padLeft( null, 6 );
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
            padRight( null, 6 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testPadFailWithNullSArgument()

    /**
     *  Tests for
     *  {@link StringUtils#pad(CharSequence, int, char, Padding, Clipping)},
     *  {@link StringUtils#padCenter(CharSequence, int)},
     *  {@link StringUtils#padLeft(CharSequence, int)},
     *  and
     *  {@link StringUtils#padRight(CharSequence, int)}.
     */
    @Test
    final void testPadFailWithInvalidLengthArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            pad( "String", 4, ' ', PADDING_CENTER, CLIPPING_ABBREVIATE_MIDDLE );
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
            pad( "String", 3, ' ', null, CLIPPING_ABBREVIATE );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testPadFailWithInvalidLengthArgument()
}
//  class TestPad

/*
 *  End of File
 */