/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.helper;

import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Provides instances of
 *  {@link SplitStringData}
 *  for testing.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version VariableNameProvider: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *
 *  @see org.tquadrat.foundation.util.StringUtils#splitString(CharSequence, CharSequence)
 *  @see org.tquadrat.foundation.util.StringUtils#splitString(CharSequence, char)
 *  @see org.tquadrat.foundation.util.StringUtils#stream(CharSequence, int)
 *  @see org.tquadrat.foundation.util.StringUtils#stream(CharSequence, CharSequence)
 *  @see org.tquadrat.foundation.util.StringUtils#stream(CharSequence, char)
 *  @see org.tquadrat.foundation.util.StringUtils#stream(CharSequence, int)
 *  @see org.tquadrat.foundation.util.StringUtils#stream(CharSequence, java.util.regex.Pattern)
 */
@ClassVersion( sourceVersion = "$Id: SplitStringProvider.java 820 2020-12-29 20:34:22Z tquadrat $" )
@UtilityClass
public final class SplitStringProvider
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private SplitStringProvider() { throw new PrivateConstructorForStaticClassCalledError( SplitStringProvider.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides test data for
     *  {@link org.tquadrat.foundation.util.stringutils.TestSplitString#testSplitString(SplitStringData)}
     *  and
     *  {@link org.tquadrat.foundation.util.stringutils.TestStream#testStream(SplitStringData)}.
     *
     *  @return The test data.
     */
    public static final Stream<SplitStringData> provideSplitStringData()
    {
        final Builder<SplitStringData> builder = Stream.builder();

        String [] expected;
        CharSequence input;

        expected = new String [] {EMPTY_STRING};
        input = EMPTY_STRING;
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {EMPTY_STRING, EMPTY_STRING};
        input = "|";
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {"eins", "zwei", "drei"};
        input = "eins|zwei|drei";
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {EMPTY_STRING, "eins", "zwei", "drei"};
        input = "|eins|zwei|drei";
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {EMPTY_STRING, "eins", "zwei", "drei", EMPTY_STRING};
        input = "|eins|zwei|drei|";
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {EMPTY_STRING, "eins", EMPTY_STRING, "zwei", EMPTY_STRING, "drei", EMPTY_STRING};
        input = "|eins||zwei||drei|";
        builder.add( new SplitStringData( expected, input ) );

        expected = new String [] {EMPTY_STRING, "eins", "zwei", "drei", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING};
        input = "|eins|zwei|drei|||";
        builder.add( new SplitStringData( expected, input ) );

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  provideSplitStringData()
}
//  class VariableNameProvider

/*
 *  End of File
 */