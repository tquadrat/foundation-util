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
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.SecurityUtils.MD5HASH_Length;
import static org.tquadrat.foundation.util.SecurityUtils.calculateMD5Hash;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SecurityUtils;

/**
 *  This test class provides some tests for the usage of the methods
 *  {@link SecurityUtils#calculateMD5Hash(CharSequence)}
 *  and
 *  {@link SecurityUtils#calculateMD5Hash(byte[])}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestMD5HashCreation.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestMD5HashCreation" )
public class TestMD5HashCreation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link SecurityUtils#calculateMD5Hash(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @SuppressWarnings( "MisorderedAssertEqualsArguments" )
    @Test
    public final void testCalculateMD5Hash() throws Exception
    {
        skipThreadTest();

        String actual, candidate;

        /*
         * null works, but returns null.
         */
        candidate = null;
        actual = calculateMD5Hash( candidate );
        assertNull( actual );

        candidate = EMPTY_STRING;
        actual = calculateMD5Hash( candidate );
        assertNotNull( actual );
        assertEquals( MD5HASH_Length, actual.length() );

        candidate = "1234567890";
        actual = calculateMD5Hash( candidate );
        assertNotNull( actual );
        assertEquals( MD5HASH_Length, actual.length() );
    }   //  testCalculateMD5Hash()

    /**
     *  Test method for the failure of
     *  {@link SecurityUtils#calculateMD5Hash(byte[])}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    public final void testCalculateMD5HashWithNullArgument() throws Exception
    {
        skipThreadTest();

        final byte [] candidate = null;

        final byte [] actual;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            actual = calculateMD5Hash( candidate );
            assertNotNull( actual );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCalculateMD5HashWithNullArgument()
}
//  class TestMD5HashCreation

/*
 *  End of File
 */