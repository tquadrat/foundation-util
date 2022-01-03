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

package org.tquadrat.foundation.util.charsetutils;

import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.MIN_CODE_POINT;
import static java.lang.Character.charCount;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isBmpCodePoint;
import static java.lang.Character.isDefined;
import static java.lang.Character.isDigit;
import static java.lang.Character.isISOControl;
import static java.lang.Character.isIdeographic;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isSupplementaryCodePoint;
import static java.lang.Character.isTitleCase;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.isValidCodePoint;
import static java.lang.Character.isWhitespace;
import static java.lang.System.err;
import static java.lang.System.out;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PlaygroundClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Counts the Unicode character belonging the various categories.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version CategorizeUnicode: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@PlaygroundClass
@ClassVersion( sourceVersion = "CategorizeUnicode: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
public final class CategorizeUnicode
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private CategorizeUnicode() { throw new PrivateConstructorForStaticClassCalledError( CategorizeUnicode.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     * The program entry point.
     *
     * @param args The command line arguments.
     */
    @SuppressWarnings( {"ThrowCaughtLocally", "ProhibitedExceptionThrown"} )
    public static final void main( final String... args )
    {
        var alphabeticCount = 0;
        var bmpCount = 0;
        var digitCount = 0;
        var ideographicCount = 0;
        var isoControlCount = 0;
        var letterCount = 0;
        var lowerCaseCount = 0;
        var multiCharCount = 0;
        var notBmpCount = 0;
        var singleCharCount = 0;
        var supplementaryCount = 0;
        var titleCaseCount = 0;
        var upperCaseCount = 0;
        var whitespaceCount = 0;

        var count = 0;

        try
        {
            SearchLoop: for( var codePoint = MIN_CODE_POINT; codePoint <= MAX_CODE_POINT; ++codePoint )
            {
                if( !isDefined( codePoint ) ) continue SearchLoop;
                if( !isValidCodePoint( codePoint ) ) continue SearchLoop;

                ++count;

                if( isAlphabetic( codePoint ) ) ++alphabeticCount;
                if( isBmpCodePoint( codePoint ) )
                {
                    ++bmpCount;
                }
                else
                {
                    ++notBmpCount;
                }
                if( isDigit( codePoint ) ) ++digitCount;
                if( isIdeographic( codePoint ) ) ++ideographicCount;
                if( isISOControl( codePoint ) ) ++isoControlCount;
                if( isLetter( codePoint ) ) ++letterCount;
                if( isLowerCase( codePoint ) ) ++lowerCaseCount;
                if( isSupplementaryCodePoint( codePoint ) ) ++supplementaryCount;
                if( isTitleCase( codePoint ) ) ++titleCaseCount;
                if( isUpperCase( codePoint ) ) ++upperCaseCount;
                if( isWhitespace( codePoint ) ) ++whitespaceCount;

                switch( charCount( codePoint ) )
                {
                    case 1 -> ++singleCharCount;
                    case 2 -> ++multiCharCount;
                    default -> throw new Error( "Too many chars – or zero ..." );
                }
            }   //  SearchLoop:

            out.printf( """
                count              = %6d
                alphabeticCount    = %6d
                bmpCount           = %6d
                digitCount         = %6d
                ideographicCount   = %6d
                isoControlCount    = %6d
                letterCount        = %6d
                lowerCaseCount     = %6d
                multiCharCount     = %6d
                notBmpCount        = %6d
                singleCharCount    = %6d
                supplementaryCount = %6d
                titleCaseCount     = %6d
                upperCaseCount     = %6d
                whitespaceCount    = %6d""", count, alphabeticCount, bmpCount,
                digitCount, ideographicCount, isoControlCount, letterCount,
                lowerCaseCount, multiCharCount, notBmpCount, singleCharCount,
                supplementaryCount, titleCaseCount, upperCaseCount,
                whitespaceCount );
        }
        catch( final Throwable t )
        {
            t.printStackTrace( err );
        }
    }  //  main()
}
//  class CategorizeUnicode

/*
 *  End of File
 */