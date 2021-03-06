/**
 * Copyright 2017 FIX Protocol Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package io.fixprotocol.orchestra.model.quickfix;

import java.util.List;

import io.fixprotocol._2016.fixrepository.FieldRefType;
import io.fixprotocol._2016.fixrepository.GroupRefType;
import io.fixprotocol._2016.fixrepository.MessageType;
import io.fixprotocol.orchestra.dsl.antlr.Evaluator;
import io.fixprotocol.orchestra.dsl.antlr.FixNode;
import io.fixprotocol.orchestra.dsl.antlr.FixValue;
import io.fixprotocol.orchestra.dsl.antlr.PathStep;
import io.fixprotocol.orchestra.dsl.antlr.Scope;
import io.fixprotocol.orchestra.dsl.antlr.ScoreException;
import io.fixprotocol.orchestra.dsl.antlr.SymbolResolver;
import quickfix.Message;

/**
 * @author Don Mendelson
 *
 */
class MessageScope extends AbstractMessageScope implements Scope {

  private final MessageType messageType;

  /**
   * Constructor
   * 
   * @param message FIX message to expose
   * @param messageType metadata about the FIX message type
   * @param repository FIX Repository contains metadata
   * @param symbolResolver used by DSL to resolve symbols
   * @param evaluator evalutes DSL expressions
   */
  public MessageScope(Message message, MessageType messageType, RepositoryAdapter repository,
      SymbolResolver symbolResolver, Evaluator evaluator) {
    super(message, repository, symbolResolver, evaluator);
    this.messageType = messageType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.fixprotocol.orchestra.dsl.antlr.FixNode#getName()
   */
  @Override
  public String getName() {
    return messageType.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.fixprotocol.orchestra.dsl.antlr.Scope#assign(io.fixprotocol.orchestra.dsl.antlr.PathStep,
   * io.fixprotocol.orchestra.dsl.antlr.FixValue)
   */
  @Override
  public FixValue<?> assign(PathStep arg0, FixValue<?> arg1) throws ScoreException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.fixprotocol.orchestra.dsl.antlr.Scope#nest(io.fixprotocol.orchestra.dsl.antlr.PathStep,
   * io.fixprotocol.orchestra.dsl.antlr.Scope)
   */
  @Override
  public void nest(PathStep arg0, Scope arg1) {
    throw new UnsupportedOperationException("Message structure is immutable");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.fixprotocol.orchestra.dsl.antlr.Scope#resolve(io.fixprotocol.orchestra.dsl.antlr.PathStep)
   */
  @Override
  public FixNode resolve(PathStep pathStep) {
    String name = pathStep.getName();
    List<Object> members = messageType.getStructure().getComponentOrComponentRefOrGroup();
    for (Object member : members) {
      if (member instanceof FieldRefType) {
        FieldRefType fieldRefType = (FieldRefType) member;
        if (fieldRefType.getName().equals(name)) {
          return resolveField(fieldRefType);
        }
      } else if (member instanceof GroupRefType) {
        GroupRefType groupRefType = (GroupRefType) member;
        if (groupRefType.getName().equals(name)) {
          return resolveGroup(pathStep, groupRefType);
        }
      }
    }
    return null;
  }

}
