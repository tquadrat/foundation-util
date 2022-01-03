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

package org.tquadrat.foundation.util.securityutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tquadrat.foundation.util.SecurityUtils.DHM_PRIME;
import static org.tquadrat.foundation.util.SecurityUtils.DHM_PRIME_MOD;
import static org.tquadrat.foundation.util.SecurityUtils.calculateDiffieHellmanEncryptionKey;
import static org.tquadrat.foundation.util.SecurityUtils.calculateDiffieHellmanPublicValue;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.SecurityUtils;

/**
 *  This test class provides some tests for the he Diffie-Hellman-Merkle
 *  methods
 *  ({@link SecurityUtils#calculateDiffieHellmanEncryptionKey(BigInteger, BigInteger, BigInteger)}
 *  and
 *  {@link SecurityUtils#calculateDiffieHellmanPublicValue(BigInteger, BigInteger, BigInteger)}
 *  from the class
 *  {@link SecurityUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestDiffieHellmannMerkle.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.TestDiffieHellmannMerkle" )
public class TestDiffieHellmannMerkle extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for the Diffie-Hellman-Merkle methods
     *  ({@link SecurityUtils#calculateDiffieHellmanEncryptionKey(BigInteger, BigInteger, BigInteger)}
     *  and
     *  {@link SecurityUtils#calculateDiffieHellmanPublicValue(BigInteger, BigInteger, BigInteger)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @org.junit.jupiter.api.Test
    public final void testDiffieHellman() throws Exception
    {
        skipThreadTest();

        BigInteger a;
        BigInteger b;
        BigInteger p;
        BigInteger g;
        BigInteger aPublic;
        BigInteger bPublic;

        BigInteger expected;
        BigInteger result;

        /*
         * The simple sample from the German Wikipedia article.
         */
        p = BigInteger.valueOf( 13 );
        g = BigInteger.valueOf( 2 );
        a = BigInteger.valueOf( 5 );
        b = BigInteger.valueOf( 7 );
        expected = BigInteger.valueOf( 6 );
        aPublic = calculateDiffieHellmanPublicValue( p, g, a );
        assertEquals( expected, aPublic );
        expected = BigInteger.valueOf( 11 );
        bPublic = calculateDiffieHellmanPublicValue( p, g, b );
        assertEquals( expected, bPublic );
        expected = BigInteger.valueOf( 7 );
        result = calculateDiffieHellmanEncryptionKey( p, a, bPublic );
        assertEquals( expected, result );
        result = calculateDiffieHellmanEncryptionKey( p, b, aPublic );
        assertEquals( expected, result );

        /*
         * A more realistic sample.
         */
        p = DHM_PRIME;
        g = DHM_PRIME_MOD;
        a = new BigInteger( "1234567890" );
        b = new BigInteger( "987654321" );
        aPublic = calculateDiffieHellmanPublicValue( p, g, a );
        bPublic = calculateDiffieHellmanPublicValue( p, g, b );
        expected = calculateDiffieHellmanEncryptionKey( p, b, aPublic );
        result = calculateDiffieHellmanEncryptionKey( p, a, bPublic );
        assertEquals( expected, result );
    }   //  testDiffieHellman()
}
//  class TestDiffieHellmannMerkle

/*
 *  End of File
 */