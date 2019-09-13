/*
 Copyright (C) 2019 Electronic Arts Inc.  All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1.  Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2.  Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3.  Neither the name of Electronic Arts, Inc. ("EA") nor the names of
     its contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY ELECTRONIC ARTS AND ITS CONTRIBUTORS "AS IS" AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL ELECTRONIC ARTS OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cloud.orbit.samples.helloconcurrency;

import cloud.orbit.actors.Actor;
import cloud.orbit.actors.Stage;

/**
 * Created by jianjunzhou@ea.com.
 * This demo is created from the helloworld example but it is designed to demo the concurrency feature in orbit framework
 */
public class Main
{
	static public void processMessagesByOneActor() {
        System.out.println("------------DEMO processing messages in sequence by one actor -----------------");
        final int total = 10;
        for( int i = 0; i < total; i++) {
        	String message = "Welcome to orbit " + i;
            System.out.println("Message to send: " + message);
            
        	// Each message is processed by the actor 0, therefore all the messages are sent in sequence and processed in sequence too. The order of processing is opposite to the order of sending. 
        	Actor.getReference(Hello.class, "0").sayHello(message).join();
        }		
	}

	static public void processMessagesByMultipleActor() {
        System.out.println("------------DEMO processing messages in sequence by multiple actor intances -----------------");
        final int total = 10;
        for( int i = 0; i < total; i++) {
        	String message = "Welcome to orbit " + i;
            System.out.println("Message to send: " + message);
            
        	// Each message is processed by a new instance of the HelloActor but in the order of receiving  
        	Actor.getReference(Hello.class, String.format("%d", i)).sayHello(message).join();
        }
	}
	
	static public void processMessagesConcurrently() {
        System.out.println("------------DEMO processing messages concurrently by multiple actor instances-----------------");
        // Send messages concurrently
        // The messages are sent in sequence but it is processed by actors concurrently, so the responses are received without orders.   
        final int total = 10;
        for( int i = 0; i < total; i++) {
        	String message = "Welcome to orbit " + i;
            System.out.println("Message to send: " + message);
            
            // Each message is processed by a new instance of the HelloActor
        	Actor.getReference(Hello.class, String.format("%d", i)).sayHello(message);
        }
		
	}	
    public static void main(String[] args) throws Exception
    {
        // Create and bind to an orbit stage
        Stage stage = new Stage.Builder().clusterName("orbit-demo-concurrency-cluster").build();
        stage.start().join();
        stage.bind();

        processMessagesByOneActor();
        processMessagesByMultipleActor();
        processMessagesConcurrently();
        
        // Shut down the stage
        stage.stop().join();
    }
}
