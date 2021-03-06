/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.lang.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.JetNodeTypes;
import org.jetbrains.jet.lexer.JetToken;

/**
 * @author max
 */
abstract class JetDeclarationImpl extends JetExpressionImpl implements JetDeclaration {
    public JetDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public JetModifierList getModifierList() {
        return (JetModifierList) findChildByType(JetNodeTypes.MODIFIER_LIST);
    }

    @Override
    public boolean hasModifier(JetToken modifier) {
        JetModifierList modifierList = getModifierList();
        return modifierList != null && modifierList.hasModifier(modifier);
    }
}
