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

import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The test data for
 *  {@link org.tquadrat.foundation.util.stringutils.TestSplitString#testSplitString(SplitStringData)}
 *  and
 *  {@link org.tquadrat.foundation.util.stringutils.TestStream#testStream(SplitStringData)}
 *
 *  @param  expected    The expected result for the test.
 *  @param  input   The input to process.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SplitStringData.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( {"hiding", "javadoc", "preview"} )
@ClassVersion( sourceVersion = "$Id: SplitStringData.java 820 2020-12-29 20:34:22Z tquadrat $" )
public record SplitStringData( String[] expected, CharSequence input )
{ /* empty */ }
//  record SplitStringData

/*
 *  End of File
 */