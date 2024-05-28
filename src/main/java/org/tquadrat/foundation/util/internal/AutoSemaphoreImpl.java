/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.util.internal;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.util.concurrent.Semaphore;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.AutoSemaphore;

/**
 *  <p>{@summary The implementation of
 *  {@link AutoSemaphore}.}</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *  @since 0.4.8
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
@API( status = STABLE, since = "0.4.8" )
public final class AutoSemaphoreImpl extends Semaphore implements AutoSemaphore
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary The token that holds the permits to be released when a
     *  {@code try-with-resources} block is left.}</p>
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
     *  @since 0.4.8
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "NewClassNamingConvention" )
    @ClassVersion( sourceVersion = "$Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
    @API( status = INTERNAL, since = "0.4.8" )
    private final class Token implements AutoCloseable
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The number of permits to release on close.
         */
        private final int m_Permits;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code Token}.
         *
         *  @param  permits The number of the acquired permits.
         */
        public Token( final int permits )
        {
            m_Permits = permits;
        }   //  Token()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final void close() throws Exception
        {
            AutoSemaphoreImpl.this.release( m_Permits );
        }   //  close()
    }
    //  class Token

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 539879857L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates an {@code AutoSemaphoreImpl} instance with the given number of
     *  permits and non-fair fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     */
    public AutoSemaphoreImpl( final int permits )
    {
        super( permits );
    }   //  AutoSemaphoreImpl()

    /**
     *  Creates an {@code AutoSemaphoreImpl} instance with the given number of
     *  permits and the given fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     *  @param  fair    {@code true} if this semaphore will guarantee first-in
     *      first-out granting of permits under contention, else {@code false}.
     */
    public AutoSemaphoreImpl( final int permits, final boolean fair )
    {
        super( permits, fair );
    }   //  AutoSemaphoreImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    @SuppressWarnings( "ReturnOfInnerClass" )
    public final AutoCloseable acquireToken( final int permits ) throws InterruptedException, IllegalArgumentException
    {
        acquire( permits );
        final var retValue = new Token( permits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  acquireToken()

    /**
     *  {@inheritDoc}
     */
    @Override
    @SuppressWarnings( "ReturnOfInnerClass" )
    public final AutoCloseable acquireTokenUninterruptibly( final int permits ) throws IllegalArgumentException
    {
        acquireUninterruptibly( permits );
        final var retValue = new Token( permits );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  acquireTokenUninterruptibly()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Semaphore getSemaphore() { return this; }
}
//  class AutoSemaphore

/*
 *  End of File
 */