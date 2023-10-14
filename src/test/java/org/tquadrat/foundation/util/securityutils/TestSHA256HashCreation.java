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

package org.tquadrat.foundation.util.securityutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.SecurityUtils.SHA256HASH_Length;
import static org.tquadrat.foundation.util.SecurityUtils.calculateSHA256Hash;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SecurityUtils;

/**
 *  This test class provides some tests for the usage of the methods
 *  {@link SecurityUtils#calculateSHA256Hash(CharSequence)}
 *  and
 *  from the class
 *  {@link SecurityUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestSHA256HashCreation.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestSHA256HashCreation" )
public class TestSHA256HashCreation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link SecurityUtils#calculateSHA256Hash(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @SuppressWarnings( "MisorderedAssertEqualsArguments" )
    @Test
    public final void testCalculateSHA256Hash() throws Exception
    {
        skipThreadTest();

        String actual, candidate;

        /*
         * null works, but returns null.
         */
        candidate = null;
        actual = calculateSHA256Hash( candidate );
        assertNull( actual );

        candidate = EMPTY_STRING;
        actual = calculateSHA256Hash( candidate );
        assertNotNull( actual );
        assertEquals( SHA256HASH_Length, actual.length() );

        candidate = "1234567890";
        actual = calculateSHA256Hash( candidate );
        assertNotNull( actual );
        assertEquals( SHA256HASH_Length, actual.length() );
    }   //  testCalculateSHA256Hash()

    /**
     *  Test method for the failure of
     *  {@link SecurityUtils#calculateSHA256Hash(byte[])}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    public final void testtestCalculateSHA256WithNullArgument() throws Exception
    {
        skipThreadTest();

        final byte [] actual;
        final byte [] candidate = null;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            actual = calculateSHA256Hash( candidate );
            assertNotNull( actual );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testtestCalculateSHA256WithNullArgument()
}
//  class TestSHA256HashCreation

/*
 *  End of File
 */