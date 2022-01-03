/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  A sealed class for testing purposes.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SealedClass.java 858 2021-01-23 15:51:52Z tquadrat $
 */
@SuppressWarnings( "AbstractClassWithoutAbstractMethods" )
@ClassVersion( sourceVersion = "$Id: SealedClass.java 858 2021-01-23 15:51:52Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public abstract sealed class SealedClass
    permits NonSealedClass
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    @SuppressWarnings( "javadoc" )
    protected SealedClass() { /* Does nothing */ }
}
//  class SealedClass

/*
 *  End of File
 */