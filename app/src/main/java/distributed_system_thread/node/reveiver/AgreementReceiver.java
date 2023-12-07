package distributed_system_thread.node.reveiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_system_thread.alg.Agreement;
import distributed_system_thread.domain.AgreementEntity;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class AgreementReceiver extends Thread {
    private final Socket socket;
    private final long nodeThreadId;
    private final Agreement agreementAlg;

    @Override
    public void run() {
        try( var inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream())) ) {
            String inputString = inputBuffer.readLine();

            Thread.sleep(100);
            socket.close();

            var mapper = new ObjectMapper().reader();
            var entity = mapper.readValue(inputString, AgreementEntity.class);
//            System.out.println("Thread " + nodeThreadId + " received " + entity);
            if(entity.getRound() == agreementAlg.getRound()) {
                agreementAlg.addChoice(entity.getChoice());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
    }
}
