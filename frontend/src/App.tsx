import {Toaster} from 'react-hot-toast';
import AppRouter from "@/routes/AppRouter.tsx";

function App() {
    return (
        <>
            <AppRouter/>
            <Toaster position="top-center"/>
        </>
    );
}

export default App;
