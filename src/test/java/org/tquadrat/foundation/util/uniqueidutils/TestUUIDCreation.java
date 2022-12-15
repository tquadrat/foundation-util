/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.util.uniqueidutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.util.SystemUtils.getNodeId;
import static org.tquadrat.foundation.util.UniqueIdUtils.HashType.HASH_MD5;
import static org.tquadrat.foundation.util.UniqueIdUtils.HashType.HASH_SHA;
import static org.tquadrat.foundation.util.UniqueIdUtils.nameUUIDFromBytes;
import static org.tquadrat.foundation.util.UniqueIdUtils.nameUUIDFromString;
import static org.tquadrat.foundation.util.UniqueIdUtils.randomUUID;
import static org.tquadrat.foundation.util.UniqueIdUtils.sequenceUUID;
import static org.tquadrat.foundation.util.UniqueIdUtils.timebasedUUID;
import static org.tquadrat.foundation.util.UniqueIdUtils.timebasedUUIDFromNodeName;
import static org.tquadrat.foundation.util.UniqueIdUtils.uuidFromString;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.UniqueIdUtils;
import org.tquadrat.foundation.util.UniqueIdUtils.HashType;

/**
 *  This class tests the UUID creation methods from
 *  {@link UniqueIdUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestUUIDCreation.java 1037 2022-12-15 00:35:17Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.uniqueidutils.TestUUIDCreation" )
public class TestUUIDCreation extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link UniqueIdUtils#timebasedUUID()},
     *  {@link UniqueIdUtils#timebasedUUID(long)},
     *  and
     *  {@link UniqueIdUtils#timebasedUUIDFromNodeName(CharSequence)}.<br>
     *  <br>Basically it will be tested that the generated UUIDs are of the
     *  right type and that they distinct from each other (as long as this can
     *  be tested).
     */
    @Test
    public final void testUUIDCreation()
    {
        skipThreadTest();

        var uuid1 = timebasedUUID();
        assertNotNull( uuid1 );
        assertEquals( 2, uuid1.variant() );
        assertEquals( 1, uuid1.version() );

        var uuid2 = timebasedUUID();
        assertNotNull( uuid2 );
        assertEquals( 2, uuid2.variant() );
        assertEquals( 1, uuid2.version() );

        assertNotEquals( uuid2, uuid1 );
        assertEquals( uuid1.node(), uuid2.node() );

        final var nodeId = getNodeId();
        uuid1 = timebasedUUID( nodeId );
        assertNotNull( uuid1 );
        assertEquals( 2, uuid1.variant() );
        assertEquals( 1, uuid1.version() );
        assertEquals( nodeId, uuid1.node() );

        uuid2 = timebasedUUID( nodeId );
        assertNotNull( uuid2 );
        assertEquals( 2, uuid2.variant() );
        assertEquals( 1, uuid2.version() );
        assertEquals( nodeId, uuid2.node() );

        assertNotEquals( uuid2, uuid1 );

        assertThrows( NullArgumentException.class, () -> timebasedUUIDFromNodeName( null ) );

        final var nodeName = "tquadrat.org";
        uuid1 = timebasedUUIDFromNodeName( nodeName );
        assertNotNull( uuid1 );
        assertEquals( 2, uuid1.variant() );
        assertEquals( 1, uuid1.version() );

        uuid2 = timebasedUUIDFromNodeName( nodeName );
        assertNotNull( uuid2 );
        assertEquals( 2, uuid2.variant() );
        assertEquals( 1, uuid2.version() );

        assertNotEquals( uuid2, uuid1 );
        assertEquals( uuid1.node(), uuid2.node() );

        String name;
        HashType hashType;
        byte [] nameBytes;

        name = null;
        nameBytes = null;
        hashType = HASH_MD5;
        try
        {
            uuid1 = nameUUIDFromString( name, hashType );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }
        try
        {
            uuid1 = nameUUIDFromBytes( nameBytes, hashType );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        name = "name";
        nameBytes = name.getBytes( UTF8 );
        hashType = null;
        try
        {
            uuid1 = nameUUIDFromString( name, hashType );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }
        try
        {
            uuid1 = nameUUIDFromBytes( nameBytes, hashType );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        hashType = HASH_MD5;
        uuid1 = nameUUIDFromString( EMPTY_STRING, hashType );
        assertNotNull( uuid1 );

        uuid1 = nameUUIDFromString( nodeName, hashType );
        assertNotNull( uuid1 );
        uuid2 = nameUUIDFromString( nodeName, hashType );
        assertNotNull( uuid2 );
        assertEquals( uuid1, uuid2 );

        uuid1 = nameUUIDFromBytes( nodeName.getBytes( UTF8 ), hashType );
        assertNotNull( uuid1 );
        assertEquals( uuid1, uuid2 );

        hashType = HASH_SHA;
        uuid1 = nameUUIDFromString( EMPTY_STRING, hashType );
        assertNotNull( uuid1 );

        uuid1 = nameUUIDFromString( nodeName, hashType );
        assertNotNull( uuid1 );
        uuid2 = nameUUIDFromString( nodeName, hashType );
        assertNotNull( uuid2 );
        assertEquals( uuid1, uuid2 );

        uuid1 = nameUUIDFromBytes( nodeName.getBytes( UTF8 ), hashType );
        assertNotNull( uuid1 );
        assertEquals( uuid1, uuid2 );

        uuid1 = randomUUID();
        assertNotNull( uuid1 );
        assertNotEquals( uuid2, uuid1 );
        final Collection<UUID> uuids = new HashSet<>();
        uuids.add( uuid1 );
        uuids.add( uuid2 );
        for( var i = 0; i < 10000; ++i )
        {
            assertTrue( uuids.add( randomUUID() ) );
        }

        long high, low;
        high = 0;
        low = 0;
        uuid1 = sequenceUUID( high, low );
        assertNotNull( uuid1 );
        uuid2 = sequenceUUID( high, low );
        assertNotNull( uuid2 );
        assertEquals( uuid1, uuid2 );

        high = Long.MAX_VALUE;
        low = Long.MIN_VALUE;
        uuid1 = sequenceUUID( high, low );
        assertNotNull( uuid1 );
        uuid2 = sequenceUUID( high, low );
        assertNotNull( uuid2 );
        assertEquals( uuid1, uuid2 );

        try
        {
            uuid1 = uuidFromString( null );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = NullArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        try
        {
            uuid1 = uuidFromString( EMPTY_STRING );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = EmptyArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        try
        {
            uuid1 = uuidFromString( "Fußpilz" );
            assertNotNull( uuid1 );
            fail( "Expected Exception was not thrown" );
        }
        catch( final Exception e )
        {
            final Class<? extends Exception> expectedException = IllegalArgumentException.class;
            final var isExpectedException = expectedException.isInstance( e );
            assertTrue( isExpectedException, format( "Wrong Exception type; caught '%2$s' but '%1$s' was expected", expectedException.getName(), e.getClass().getName() ) );
        }

        uuid2 = randomUUID();
        uuid1 = uuidFromString( uuid2.toString() );
        assertNotNull( uuid1 );
        assertEquals( uuid1, uuid2 );
    }   //  testUUIDCreation()
}
//  class TestUUIDCreation

/*
 *  End of File
 */