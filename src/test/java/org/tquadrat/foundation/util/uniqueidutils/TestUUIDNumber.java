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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tquadrat.foundation.util.UniqueIdUtils.uuidFromNumber;
import static org.tquadrat.foundation.util.UniqueIdUtils.uuidToNumber;

import java.math.BigInteger;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.UniqueIdUtils;

/**
 *  Tests for
 *  {@link org.tquadrat.foundation.util.UniqueIdUtils#uuidToNumber(UUID)}
 *  and
 *  {@link org.tquadrat.foundation.util.UniqueIdUtils#uuidFromNumber(BigInteger)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestUUIDNumber.java 1037 2022-12-15 00:35:17Z tquadrat $
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TestUUIDNumber.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestUUIDNumber" )
public class TestUUIDNumber extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Provides the UUIDs for the conversion tests.
     *
     *  @return A stream with random UUID.
     */
    static final Stream<UUID> randomValueProvider()
    {
        final var retValue = Stream.generate( UniqueIdUtils::randomUUID )
            .limit( 1000 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  randomValueProvider()

    /**
     *  Validation tests for
     *  {@link org.tquadrat.foundation.util.UniqueIdUtils#uuidToNumber(UUID)}
     *  and
     *  {@link org.tquadrat.foundation.util.UniqueIdUtils#uuidFromNumber(BigInteger)}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    final void testArgumentValidation() throws Exception
    {
        skipThreadTest();

        assertThrows( NullArgumentException.class, () -> uuidFromNumber( null ) );
        assertThrows( NullArgumentException.class, () -> uuidToNumber( null ) );
    }   //  testArgumentValidation()

    /**
     *  Tests the conversion.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @ParameterizedTest
    @MethodSource( "org.tquadrat.foundation.util.uniqueidutils.TestUUIDNumber#randomValueProvider" )
    final void testConversion( final UUID uuid ) throws Exception
    {
        skipThreadTest();

        final var number = uuidToNumber( uuid );
        assertNotNull( number );
        assertEquals( uuid, uuidFromNumber( number ) );
    }   //  testConversion()
}
//  class TestUUIDNumber

/*
 *  End of File
 */