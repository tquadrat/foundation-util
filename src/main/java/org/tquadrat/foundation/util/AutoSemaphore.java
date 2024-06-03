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

package org.tquadrat.foundation.util;

import static org.apiguardian.api.API.Status.STABLE;

import java.time.Duration;
import java.util.concurrent.Semaphore;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.internal.AutoSemaphoreImpl;
import org.tquadrat.foundation.util.internal.TimeoutSemaphoreImpl;

/**
 *  <p>{@summary An implementation of
 *  {@link Semaphore}
 *  that allows to be used with {@code try-with-resources}.}</p>
 *  <p>Use this class like this:</p>
 *  <pre><code>
 *  final static final int MAX_AVAILABLE = …
 *  final AutoSemaphore semaphore = AutoSemaphore.of( MAX_AVAILABLE );
 *  …
 *  try( final var token = semaphore.acquireToken() )
 *  {
 *      // Do something …
 *  }
 *  catch( final InterruptedException e )
 *  {
 *      // Handle the exception …
 *  }
 *  </code></pre>
 *  <p>A call to
 *  {@link #of(int,Duration)}
 *  or
 *  {@link #of(int,boolean,Duration)}
 *  creates a {@code AutoSemaphore} instance whose permits will be released
 *  automatically after the given period of time. Do not acquire permits from
 *  the instance returned by a call to
 *  {@link #getSemaphore()}
 *  on such an instance, as it may behave unexpectedly.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AutoSemaphore.java 1136 2024-05-30 18:25:38Z tquadrat $
 *  @since 0.4.8
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: AutoSemaphore.java 1136 2024-05-30 18:25:38Z tquadrat $" )
@API( status = STABLE, since = "0.4.8" )
public sealed interface AutoSemaphore
    permits AutoSemaphoreImpl, TimeoutSemaphoreImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Acquires a permit from this semaphore, blocking until one
     *  is available, or the thread is
     *  {@linkplain Thread#interrupt interrupted},
     *  and returns a token object that is used for
     *  {@code try-with-resources}.}</p>
     *  <p>Acquires a permit, if one is available and returns immediately,
     *  reducing the number of available permits by one.</p>
     *  <p>If no permit is available then the current thread becomes disabled
     *  for thread scheduling purposes and lies dormant until one of two things
     *  happens:</p>
     *  <ul>
     *    <li>Some other thread invokes the
     *    {@link Semaphore#release() release()}
     *    method for this semaphore and the current thread is next to be
     *    assigned a permit; or</li>
     *    <li>Some other thread
     *    {@linkplain Thread#interrupt interrupts}
     *    the current thread.</li>
     *  </ul>
     *  <p>If the current thread:
     *  <ul>
     *    <li>has its interrupted status set on entry to this method; or</li>
     *    <li>is
     *    {@linkplain Thread#interrupt interrupted}
     *    while waiting for a permit,
     *  </ul>
     *  then
     *  {@link InterruptedException}
     *  is thrown and the current thread's interrupted status is cleared.
     *
     *  @return The token.
     *  @throws InterruptedException    The current thread was interrupted.
     */
    public default AutoCloseable acquireToken() throws InterruptedException { return acquireToken( 1 ); }

    /**
     *  <p>{@summary Acquires the given number of permits from this semaphore,
     *  blocking until all are available, or the thread is
     *  {@linkplain Thread#interrupt interrupted},
     *  and returns a token object that is used for
     *  {@code try-with-resources}.}</p>
     *   <p>Acquires the given number of permits, if they are available, and
     *   returns immediately, reducing the number of available permits by the
     *   given amount. This method atomically acquires the permits all at
     *   once.</p>
     *   <p>If insufficient permits are available then the current thread
     *   becomes disabled for thread scheduling purposes and lies dormant until
     *   one of two things happens:</p>
     *   <ul>
     *       <li>Some other thread invokes one of the
     *       {@link Semaphore#release() release()}
     *       methods for this semaphore and the current thread is next to be
     *       assigned permits and the number of available permits satisfies
     *       this request; or</li>
     *       <li>Some other thread
     *       {@linkplain Thread#interrupt interrupts}
     *       the current thread.</li>
     *  </ul>
     *  <p>If the current thread:
     *  <ul>
     *      <li>has its interrupted status set on entry to this method; or</li>
     *      <li>is
     *      {@linkplain Thread#interrupt interrupted}
     *      while waiting for a permit,
     *  </ul>
     *  <p>then an
     *  {@link InterruptedException}
     *  is thrown and the current thread's interrupted status is cleared. Any
     *  permits that were to be assigned to this thread are instead assigned to
     *  other threads trying to acquire permits, as if permits had been made
     *  available by a call to
     *  {@link Semaphore#release() release()}.</p>
     *
     *  @param  permits The number of permits to acquire.
     *  @return The token.
     *  @throws InterruptedException    The current thread is interrupted.
     *  @throws IllegalArgumentException    The given number of permits to
     *      acquire is negative.
     */
    public AutoCloseable acquireToken( final int permits ) throws InterruptedException, IllegalArgumentException;

    /**
     *  <p>{@summary Acquires a permit from this semaphore, blocking until one
     *  is available, and returns a token object that is used for
     *  {@code try-with-resources}.}</p>
     *  <p>Acquires a permit, if one is available and returns immediately,
     *  reducing the number of available permits by one.</p>
     *  <p>If no permit is available then the current thread becomes disabled
     *  for thread scheduling purposes and lies dormant until some other thread
     *  invokes the
     *  {@link Semaphore#release() release()}
     *  method for this semaphore and the current thread is next to be assigned
     *  a permit.</p>
     *  <p>If the current thread is
     *  {@linkplain Thread#interrupt interrupted}
     *  while waiting for a permit then it will continue to wait, but the time
     *  at which the thread is assigned a permit may change compared to the
     *  time it would have received the permit had no interruption occurred.
     *  When the thread does return from this method its interrupt status will
     *  be set.</p>
     *
     *  @return The token.
     */
    public default AutoCloseable acquireTokenUninterruptibly() { return acquireTokenUninterruptibly( 1 ); }

    /**
     *  <p>{@summary Acquires the given number of permits from this semaphore,
     *  blocking until all are available, and returns a token object that is
     *  used for {@code try-with-resources}.}</p>
     *  <p>Acquires the given number of permits, if they are available, and
     *  returns immediately, reducing the number of available permits by the
     *  given amount. This method atomically acquires the permits all at
     *  once.</p>
     *  <p>If insufficient permits are available then the current thread
     *  becomes disabled for thread scheduling purposes and lies dormant until
     *  some other thread invokes one of the
     *  {@link Semaphore#release() release()}
     *  methods for this semaphore and the current thread is next to be
     *  assigned permits and the number of available permits satisfies this
     *  request.</p>
     *  <p>If the current thread is
     *  {@linkplain Thread#interrupt interrupted}
     *  while waiting for permits then it will continue to wait and its
     *  position in the queue is not affected.  When the thread does return
     *  from this method its interrupt status will be set.
     *
     *  @param  permits The number of permits to acquire.
     *  @return The token.
     *  @throws IllegalArgumentException    The given number of permits to
     *      acquire is negative.
     */
    public AutoCloseable acquireTokenUninterruptibly( final int permits ) throws IllegalArgumentException;

    /**
     *  Returns a reference to the raw semaphore.
     *
     *  @return The semaphore.
     */
    public Semaphore getSemaphore();

    /**
     *  Creates an {@code AutoSemaphore} instance with the given number of
     *  permits and non-fair fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     *  @return The new {@code AutoSemaphore} instance.
     */
    public static AutoSemaphore of( final int permits ) { return new AutoSemaphoreImpl( permits ); }

    /**
     *  Creates an {@code AutoSemaphore} instance with the given number of
     *  permits and the given fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     *  @param  fair    {@code true} if this semaphore will guarantee first-in
     *      first-out granting of permits under contention, else {@code false}.
     *  @return The new {@code AutoSemaphore} instance.
     */
    public static AutoSemaphore of( final int permits, final boolean fair ) { return new AutoSemaphoreImpl( permits, fair ); }

    /**
     *  Creates an {@code AutoSemaphore} instance with the given number of
     *  permits and non-fair fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     *  @param  duration    The timeout for a permit; after the given period of
     *      time, an acquired permit will be released automatically.
     *  @return The new {@code AutoSemaphore} instance.
     */
    public static AutoSemaphore of( final int permits, final Duration duration ) { return new TimeoutSemaphoreImpl( permits, duration ); }

    /**
     *  Creates an {@code AutoSemaphore} instance with the given number of
     *  permits and the given fairness setting.
     *
     *  @param  permits The initial number of permits available. This value may
     *      be negative, in which case releases must occur before any acquires
     *      will be granted.
     *  @param  fair    {@code true} if this semaphore will guarantee first-in
     *      first-out granting of permits under contention, else {@code false}.
     *  @param  duration    The timeout for a permit; after the given period of
     *      time, an acquired permit will be released automatically.
     *  @return The new {@code AutoSemaphore} instance.
     */
    public static AutoSemaphore of( final int permits, final boolean fair, final Duration duration ) { return new TimeoutSemaphoreImpl( permits, fair, duration ); }
}
//  class AutoSemaphore

/*
 *  End of File
 */