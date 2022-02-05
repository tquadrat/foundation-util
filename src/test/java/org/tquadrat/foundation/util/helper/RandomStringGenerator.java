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

import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.MIN_CODE_POINT;
import static java.lang.Character.isISOControl;
import static java.lang.Character.isIdeographic;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.toChars;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.util.SystemUtils.getRandom;

import java.util.stream.Stream;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Provides a generator for random Strings.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: RandomStringGenerator.java 1009 2022-02-05 09:03:15Z tquadrat $
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: RandomStringGenerator.java 1009 2022-02-05 09:03:15Z tquadrat $" )
public final class RandomStringGenerator
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private RandomStringGenerator() { throw new PrivateConstructorForStaticClassCalledError( RandomStringGenerator.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates a random String with a length between 5 and 10 characters.
     *
     *  @return A random String.
     */
    private static final String createRandomString()
    {
        final var random = getRandom();
        final long length = random.nextInt( 6 ) + 5;

        final var retValue = random.ints( MIN_CODE_POINT, MAX_CODE_POINT )
            .filter( Character::isValidCodePoint )
            .filter( Character::isDefined )
            .filter( codePoint -> !isISOControl( codePoint ) )
            .filter( codePoint -> !isWhitespace( codePoint ) )
            .filter( Character::isLetter )
            .filter( Character::isBmpCodePoint )
            .filter( codePoint -> !isIdeographic( codePoint ) )
            .limit( length )
            .mapToObj( codePoint -> new String( toChars( codePoint ) ) )
            .collect( joining() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createRandomString()

    /**
     *  Generates a
     *  {@link Stream}
     *  with the given number of random String. They are not ordered, and there
     *  are no duplicates.
     *
     *  @param  count   The desired number of Strings.
     *  @return The stream with the Strings.
     */
    public static final Stream<String> generateStream( final int count )
    {
        final var retValue = generate( RandomStringGenerator::createRandomString )
            .distinct()
            .unordered()
            .filter( StringUtils::isNotEmptyOrBlank )
            //.peek( out::println )
            .limit( count );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  generateStream()

    /**
     *  Generate the test data for the comparator tests. This is an array of
     *  random Strings that are not ordered and that does not contain
     *  duplicates.
     *
     *  @param  length  The intended length of the test data array.
     *  @return The test data.
     */
    public static final String [] generateTestData( final int length )
    {
        final var retValue = generateStream( length )
            .toArray( String []::new );

        assertTrue( stream( retValue ).allMatch( StringUtils::isNotEmptyOrBlank ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  generateTestData()

    /**
     *  A method that works as {@code MethodSource} for JUnit.
     *
     *  @return The Strings.
     */
    public static final Stream<String> provideStrings() { return generateStream( 1000 ); }
}
//  class RandomStringGenerator

/*
 *  End of File
 */