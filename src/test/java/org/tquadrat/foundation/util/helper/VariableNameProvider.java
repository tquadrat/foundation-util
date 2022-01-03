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
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.stream.Stream;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Provides variable names for testing.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version VariableNameProvider: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *
 *  @see org.tquadrat.foundation.util.Template
 */
@ClassVersion( sourceVersion = "$Id: VariableNameProvider.java 820 2020-12-29 20:34:22Z tquadrat $" )
@UtilityClass
public final class VariableNameProvider
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The container for the test data.
     *
     *  @param  valid   {@code true} if the given variable name is valid,
     *      {@code false} otherwise.
     *  @param  variableName    The variable name.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     */
    @SuppressWarnings( {"PublicInnerClass", "hiding", "javadoc", "preview"} )
    @ClassVersion( sourceVersion = "$Id: VariableNameProvider.java 820 2020-12-29 20:34:22Z tquadrat $" )
    public static record Result( boolean valid, String variableName ) { /* Empty */ }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private VariableNameProvider() { throw new PrivateConstructorForStaticClassCalledError( VariableNameProvider.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides  a
     *  {@link Stream}
     *  of variable names for testing.
     *
     *  @return The input for the tests.
     */
    public static final Stream<Result> provideVariableNames()
    {
        final char [] validPrefixes = {'~', '/', '=', '%', '&', ':' };
        final char [] invalidPrefixes = { '#', '+', '*', '$', '§', '_', '-', '.' };
        final String [] validNames = { "name", "n1name", "k22", "e3.4", "name.1", "name_2", "n", "A1", "Var1", "Var2", "\u043D\u0430\u0437\u0432\u0430\u0301\u043D\u0438\u0435" };
        final String [] invalidNames = { "123", "1name", "_k22", ".e3.4", ".", "-", "(name)" };

        final Stream.Builder<Result> builder = Stream.builder();

        {
            var name = EMPTY_STRING;
            builder.add( new Result( false, name ) );
            for( final var prefix : validPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
            for( final var prefix : invalidPrefixes )
            {
                if( prefix != '_' )
                {
                    builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
                }
            }

            name = "_";
            builder.add( new Result( true, name ) );
            for( final var prefix : validPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
            for( final var prefix : invalidPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
        }

        for( final var name : validNames )
        {
            builder.add( new Result( true, name ) );
            for( final var prefix : validPrefixes )
            {
                builder.add( new Result( true, format( "%c%s", prefix, name ) ) );
            }
            for( final var prefix : invalidPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
        }

        for( final var name : invalidNames )
        {
            builder.add( new Result( false, name ) );
            for( final var prefix : validPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
            for( final var prefix : invalidPrefixes )
            {
                builder.add( new Result( false, format( "%c%s", prefix, name ) ) );
            }
        }

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  provideVariableNames()
}
//  class VariableNameProvider

/*
 *  End of File
 */