import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";
import CountdownTimer from "@/components/layout/CountdownTimer.tsx";
import TagAccessCards from "@/components/layout/TagAccessCards.tsx";

export default function HomePage() {
    return (
        <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
            <HamburgerMenu/>

            {/* Main Content */}
            <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                {/* Band Name */}
                <h1
                    className="text-7xl md:text-[8rem] mb-8 text-center font-crimson"
                >
                    {['O', 'S', 'E', 'T'].map((letter, index) => (
                        <span
                            key={index}
                            style={{letterSpacing: index === 3 ? '0' : '0.3em'}} // no spacing after last letter
                        >{letter}
                        </span>
                    ))}
                </h1>

                <CountdownTimer
                    targetDate="2025-11-16T18:00:00"
                    targetLocation="Gdańsk, Klub xyz"
                    showEventDate={true}
                    albumTitle="Nowy Album"
                    albumLink="https://linktr.ee/example"
                    ticketLink="https://ticketlink.example.com"
                />
            </div>

            {/* Tag Access Cards Section */}
            <div className="w-full flex justify-center py-12">
                <TagAccessCards/>
            </div>
        </div>
    );
}